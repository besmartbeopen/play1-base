package common.data.charts;

import com.google.common.collect.ImmutableList;
import common.dto.LabelDecimal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.val;
import models.common.base.Steadies;

/**
 * Dati per il grafico a torta/ciambella.
 *
 * @author marco
 *
 */
@Builder
@Getter
public class DonutChartData {

  private BigDecimal quantity;
  private BigDecimal percentage;
  private String name;
  private String id;
  private String color;


  public static List<DonutChartData> convert(Collection<LabelDecimal> items) {
    val total = items.stream().map(LabelDecimal::getValue).reduce(BigDecimal.ZERO, BigDecimal::add);
    return items.stream().map(item -> DonutChartData.builder()
        .name(item.getLabel())
        .id("rnd-" + item.hashCode()) // migliorabile...
        .quantity(item.getValue())
        .percentage(item.getValue().multiply(Steadies.HUNDRED).divide(total, 2, RoundingMode.HALF_UP)
            .setScale(2, BigDecimal.ROUND_HALF_EVEN))
        .build())
        .collect(ImmutableList.toImmutableList());
  }
}
