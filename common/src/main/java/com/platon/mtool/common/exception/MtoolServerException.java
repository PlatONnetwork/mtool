package com.platon.mtool.common.exception;

/**
 * mtool服务端异常
 *
 * <p>Created by liyf.
 */
public class MtoolServerException extends MtoolException {

  private static final long serialVersionUID = 7235708326504559767L;

  public MtoolServerException(String message) {
    super(message);
  }

  public MtoolServerException(String message, Object... args) {
    super(message, args);
  }

  public MtoolServerException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
