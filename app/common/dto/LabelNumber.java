package common.dto;

import common.TemplateExtensions;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LabelNumber {
  private final String label;
  private final Number value;

  public LabelNumber(Enum<?> label, Number value) {
    this(TemplateExtensions.label(label), value);
  }
}
