package com.platon.mtool.common.exception;

/**
 * mtool客户端异常
 *
 * <p>Created by liyf.
 */
public class MtoolClientException extends MtoolException {

  public static final int PROPOSAL_PREFIX = 10000;
  private static final long serialVersionUID = -627929127911645718L;

  public MtoolClientException(String message) {
    super(message);
  }

  public MtoolClientException(String message, Object... args) {
    super(message, args);
  }
}
