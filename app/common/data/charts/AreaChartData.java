package common.data.charts;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Getter;

/**
 * Dati per i grafici ad area.
 *
 * @author marco
 *
 */
@Builder
@Getter
public class AreaChartData<D, N extends Number> {

  private D date;
  private String name;
  private N value;

  public static <J, K extends Number> List<AreaChartData<J, K>> create(String name,
      Map<J, K> values) {

    return values.entrySet().stream()
        .map(v -> AreaChartData.<J, K>builder()
            .date(v.getKey())
            .name(name)
            .value(v.getValue())
            .build())
      .collect(ImmutableList.toImmutableList());
  }
}
