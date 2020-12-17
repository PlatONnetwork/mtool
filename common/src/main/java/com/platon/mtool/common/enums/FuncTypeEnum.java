package com.platon.mtool.common.enums;

import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.exception.MtoolException;

/**
 * 交易类型枚举
 *
 * <p>Created by liyf.
 */
public enum FuncTypeEnum {
  CREATE_RESTRICTING(4000, "create_restricting", AllCommands.CREATE_RESTRICTING),
  STAKING(1000, "staking", AllCommands.STAKING),
  DECLARE_VERSION(2004, "declare_version", AllCommands.DECLARE_VERSION),
  INCREASESTAKING(1002, "increasestaking", AllCommands.INCREASESTAKING),
  SUBMIT_VERSION_PROPOSAL(2001, "submit_versionproposal", AllCommands.SUBMIT_VERSION_PROPOSAL),
  SUBMIT_TEXT_PROPOSAL(2000, "submit_textproposal", AllCommands.SUBMIT_TEXT_PROPOSAL),
  SUBMIT_CANCEL_PROPOSAL(2005, "submit_cancelproposal", AllCommands.SUBMIT_CANCEL_PROPOSAL),
  SUBMIT_PARAM_PROPOSAL(2002, "submit_paramproposal", AllCommands.SUBMIT_PARAM_PROPOSAL),
  UNSTAKING(1003, "unstaking", AllCommands.UNSTAKING),
  UPDATE_VALIDATOR(1001, "update_validator", AllCommands.UPDATE_VALIDATOR),
  VOTE_VERSION_PROPOSAL(2003, "vote_versionproposal", AllCommands.VOTE_VERSION_PROPOSAL),
  VOTE_TEXT_PROPOSAL(2003, "vote_textproposal", AllCommands.VOTE_TEXT_PROPOSAL),
  VOTE_CANCEL_PROPOSAL(2003, "vote_cancelproposal", AllCommands.VOTE_CANCEL_PROPOSAL),
  VOTE_PARAM_PROPOSAL(2003, "vote_paramproposal", AllCommands.VOTE_PARAM_PROPOSAL),
  TRANSFER(-1000, "transfer", AllCommands.Tx.TRANSFER),
  DELEGATE(1004, "delegate", AllCommands.Tx.DELEGATE),
  ;
  private int code;
  private String title;
  private String commandName;

  FuncTypeEnum(int code, String title, String commandName) {
    this.code = code;
    this.title = title;
    this.commandName = commandName;
  }

  /**
   * 该方法在投票时会有问题， 投票一个code.
   *
   * @param code 码值
   * @return FuncTypeEnum
   */
  public static FuncTypeEnum getFromCode(int code) {
    for (FuncTypeEnum funcTypeEnum : FuncTypeEnum.values()) {
      if (funcTypeEnum.getCode() == code) {
        return funcTypeEnum;
      }
    }
    throw new MtoolException("FuncTypeEnum not found", code);
  }

  public static FuncTypeEnum getFromCommandName(String commandName) {
    for (FuncTypeEnum funcTypeEnum : FuncTypeEnum.values()) {
      if (funcTypeEnum.getCommandName().equals(commandName)) {
        return funcTypeEnum;
      }
    }
    throw new MtoolException("FuncTypeEnum not found %s", commandName);
  }

  public int getCode() {
    return code;
  }

  public String getTitle() {
    return title;
  }

  public String getCommandName() {
    return commandName;
  }
}
