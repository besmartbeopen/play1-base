package common.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LabelDecimal {
  private final String label;
  private final BigDecimal value;
}
