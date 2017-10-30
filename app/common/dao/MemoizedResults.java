package common.dao;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import common.ModelQuery.SimpleResults;

/**
 * @author marco
 *
 * @param <T>
 */
public class MemoizedResults<T> {

	private static final int FIRST_ITEMS = 5;

	private final Supplier<Long> count;
	private final Supplier<Collection<T>> partial;

	MemoizedResults(final Supplier<SimpleResults<T>> results) {

		count = Suppliers.memoize(new Supplier<Long>() {
			@Override
			public Long get() {
				return results.get().count();
			}
		});
		partial = Suppliers.memoize(new Supplier<Collection<T>>() {
			@Override
			public List<T> get() {
				return results.get().list(FIRST_ITEMS);
			}
		});
	}

	public long getCount() {
		return count.get();
	}

	public Collection<T> getPartialList() {
		return partial.get();
	}

	public static <T> MemoizedResults<T> memoize(Supplier<SimpleResults<T>> r) {
		return new MemoizedResults<>(r);
	}
}
