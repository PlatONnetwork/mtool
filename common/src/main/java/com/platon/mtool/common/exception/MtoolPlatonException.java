package com.platon.mtool.common.exception;

/**
 * mtool区块链异常
 *
 * <p>Created by liyf.
 */
public class MtoolPlatonException extends MtoolException {

  private static final long serialVersionUID = -8979221367652261227L;
  private Integer code;

  public MtoolPlatonException(Integer code, String message) {
    super(message);
    this.code = code;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }
}
