package common.jpa;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import play.Play;
import play.PlayPlugin;
import play.data.binding.Binder;
import play.data.binding.Global;
import play.data.binding.RootParamNode;
import play.data.binding.TypeUnbinder;
import play.db.jpa.JPA;
import play.db.jpa.JPABase;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

/**
 * @author marco
 *
 */
@Slf4j
public class JpaReferencePlugin extends PlayPlugin {

	private static Map<Class<?>, Class<TypeUnbinder<?>>> unbinders;

	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationStart() {
		unbinders = Maps.newHashMap();
        for (Class<TypeUnbinder<?>> c : Play.classloader
        		.getAssignableClasses(TypeUnbinder.class)) {
        	if (c.isAnnotationPresent(Global.class)) {
                final Type forType = ((ParameterizedType) c
                		.getGenericInterfaces()[0]).getActualTypeArguments()[0];
                final Class<?> clsType;
                if (forType instanceof ParameterizedType) {
                	clsType = (Class<?>) ((ParameterizedType) forType).getRawType();
                } else {
                	clsType = (Class<?>) forType;
                }
                unbinders.put(clsType, c);
            }
        }
	}

	/*
	 * Find global unbinder registered for `src` object.
	 */
	@Override
    public Map<String, Object> unBind(Object src, String name) {
		if (src == null) {
			return null;
		}
		final Class<?> clazz = src.getClass();
		for (Class<?> cls : ImmutableList.of(clazz, clazz.getSuperclass())) {
			if (unbinders.containsKey(cls)) {
				final Class<TypeUnbinder<?>> unbinder = unbinders.get(cls);
				final Map<String, Object> result = Maps.newHashMap();
				try {
					if (unbinder.newInstance().unBind(result, src, clazz, name,
							new Annotation[]{})) {
						return result;
					}
				} catch (Exception e) {
					log.error("unbind error", e);
				}
			}
		}
        return null;
    }

    @Override
    public Object bind(RootParamNode rootParamNode, String name, @SuppressWarnings("rawtypes") Class clazz,
    		Type type, Annotation[] annotations) {

    	if (Optional.class.isAssignableFrom(clazz)) {
    		if (rootParamNode.getChild(name) == null) {
    			return Optional.absent();
    		} else {
    			final Class<?> cls = (Class<?>) ((ParameterizedType) type)
    					.getActualTypeArguments()[0];
    			// casi a parte per i non nullable...
    			if (JPABase.class.isAssignableFrom(cls)) {
    				final String value = rootParamNode.getChild(name)
    						.getFirstValue(Integer.class);
    				if (Strings.isNullOrEmpty(value)) {
    					return Optional.absent();
    				} else {
						try {
							// si passa come riferimento, senza .id
							int key = (Integer) Binder.directBind(value, int.class);
	    					return Optional.of(JPA.em().getReference(cls, key));
						} catch (Exception e) {
							return null;
						}
    				}
    			} else {
    				return Optional.fromNullable(Binder.bind(rootParamNode,
    						name, cls, cls, annotations));
    			}
    		}
    	}
    	return null;
    }
}
