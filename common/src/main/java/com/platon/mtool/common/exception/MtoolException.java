package com.platon.mtool.common.exception;

/**
 * mtool异常封装，有三个实现， 底层异常， mtool客户端异常， mtool服务端异常
 * Created by liyf. */
public class MtoolException extends RuntimeException {

  private static final long serialVersionUID = 3943692030840360254L;

  private String message;

  public MtoolException(String message) {
    this.message = message;
  }

  public MtoolException(Throwable cause) {
    super(cause);
    this.message = cause.getMessage();
  }

  public MtoolException(String message, Object... args) {
    this.message = String.format(message, args);
  }

  public MtoolException(String message, Throwable throwable) {
    super(message, throwable);
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
