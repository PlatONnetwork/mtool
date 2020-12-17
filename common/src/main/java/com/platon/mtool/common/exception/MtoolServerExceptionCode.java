package com.platon.mtool.common.exception;

/**
 * mtool服务端异常码
 *
 * <p>Created by liyf.
 */
public enum MtoolServerExceptionCode implements MtoolExceptionFactory<MtoolClientException> {
  CRAWLER_DOWNLOAD_FAIL(1, "crawler fetch error. type: %s, key: %s"),
  BEAN_NOT_FOUND(2, "bean not found"),
  UNKNOW_PLATON_ERROR(3, "unknow platon error");

  private int code;
  private String title;

  MtoolServerExceptionCode(int code, String title) {
    this.code = code;
    this.title = title;
  }

  @Override
  public MtoolClientException create() {
    return new MtoolClientException(title);
  }

  @Override
  public MtoolClientException create(Object... args) {
    return new MtoolClientException(String.format(title, args));
  }

  public int getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }
}
