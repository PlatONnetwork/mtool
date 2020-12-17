package com.platon.mtool.common.logger;

/**
 * 日志
 *
 * <p>Created by liyf.
 */
public class Log {

  private String textMsg;

  protected Log(String textMsg) {
    this.textMsg = textMsg;
  }

  public static Builder newBuilder() {
    return new LogBuilderImpl();
  }

  public String getTextMsg() {
    return textMsg;
  }

  public interface Builder {

    Builder op(String op);

    Builder kv(String key, Object value);

    Builder msg(String msg);

    Log build();
  }
}
