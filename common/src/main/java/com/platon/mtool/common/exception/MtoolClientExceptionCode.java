package com.platon.mtool.common.exception;

import com.alaya.contracts.ppos.dto.common.ProposalType;

/**
 * mtool客户端异常码
 *
 * <p>Created by liyf.
 */
public enum MtoolClientExceptionCode implements MtoolExceptionFactory<MtoolClientException> {
  BALANCE_NO_ENOUGH(
      MtoolPlatonExceptionCode.E301111.getCode(), MtoolPlatonExceptionCode.E301111.getMessage()),

  PROPOSAL_NO_FOUND(
      MtoolPlatonExceptionCode.E302006.getCode(), MtoolPlatonExceptionCode.E302006.getMessage()),
  // 10000
  NOT_CANCEL_PROPOSAL(
      MtoolClientException.PROPOSAL_PREFIX + ProposalType.CANCEL_PROPOSAL,
      "Non-cancel proposal ID，sending transaction failed"),
  NOT_TEXT_PROPOSAL(
      MtoolClientException.PROPOSAL_PREFIX + ProposalType.TEXT_PROPOSAL,
      "Non-text proposal ID，sending transaction failed"),
  NOT_VERSION_PROPOSAL(
      MtoolClientException.PROPOSAL_PREFIX + ProposalType.VERSION_PROPOSAL,
      "Non-version proposal ID，sending transaction failed"),
  NOT_PARAM_PROPOSAL(
      MtoolClientException.PROPOSAL_PREFIX + ProposalType.PARAM_PROPOSAL,
      "Non-param proposal ID，sending transaction failed"),
  CANCEL_PROPOSAL_NOT_SUPPORT(
      MtoolClientException.PROPOSAL_PREFIX + 100,
      "Cancel proposal only support version proposal or param proposal"),
  PARAM_VALUE_NOT_BE_SAME(
      MtoolPlatonExceptionCode.E302034.getCode(), MtoolPlatonExceptionCode.E302034.getMessage()),
  PROPOSAL_CANT_VOTE(
      MtoolPlatonExceptionCode.E302026.getCode(), MtoolPlatonExceptionCode.E302026.getMessage()),

  COMMAND_NOT_FOUND(1000, "command not found"),
  FILE_NOT_FOUND(1001, "No such file"),
  ;

  private Integer code;
  private String title;

  MtoolClientExceptionCode(Integer code, String title) {
    this.code = code;
    this.title = title;
  }

  public static MtoolClientExceptionCode getFromCode(Integer code) {
    for (MtoolClientExceptionCode value : MtoolClientExceptionCode.values()) {
      if (value.getCode().equals(code)) {
        return value;
      }
    }
    throw new MtoolPlatonException(-1, "Error code not found: " + code);
  }

  @Override
  public MtoolClientException create() {
    return new MtoolClientException(title);
  }

  @Override
  public MtoolClientException create(Object... args) {
    return new MtoolClientException(String.format(title, args));
  }

  public Integer getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }
}
