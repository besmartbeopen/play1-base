package common.gitlab.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(of="name")
public class UploadFile {

  private final String name;
  private final byte[] data;

  public UploadFile(String name, byte[] data) {
    this.name = name;
    this.data = data;
  }
}
