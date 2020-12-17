package com.platon.mtool.common;

/**
 * mtool命令定义
 *
 * <p>Created by liyf.
 */
public final class AllCommands {

  public static final String STAKING = "staking";
  public static final String DECLARE_VERSION = "declare_version";
  public static final String INCREASESTAKING = "increasestaking";
  public static final String SUBMIT_VERSION_PROPOSAL = "submit_versionproposal";
  public static final String SUBMIT_TEXT_PROPOSAL = "submit_textproposal";
  public static final String SUBMIT_CANCEL_PROPOSAL = "submit_cancelproposal";
  public static final String SUBMIT_PARAM_PROPOSAL = "submit_paramproposal";
  public static final String UNSTAKING = "unstaking";
  public static final String UPDATE_VALIDATOR = "update_validator";
  public static final String VOTE_VERSION_PROPOSAL = "vote_versionproposal";
  public static final String VOTE_TEXT_PROPOSAL = "vote_textproposal";
  public static final String VOTE_CANCEL_PROPOSAL = "vote_cancelproposal";
  public static final String VOTE_PARAM_PROPOSAL = "vote_paramproposal";
  public static final String CREATE_OBSERVEWALLET = "create_observewallet";
  public static final String OFFLINESIGN = "offlinesign";
  public static final String SEND_SIGNEDTX = "send_signedtx";
  public static final String CREATE_RESTRICTING = "create_restricting";
  // 转账
  public static final String TRANSFER = "transfer";

  private AllCommands() {}

  public static final class Account {
    // 创建钱包
    public static final String NEW = "new";
    // 私钥恢复成钱包
    public static final String RECOVER = "recover";
    // 查看钱包列表
    public static final String LIST = "list";
    // 查看钱包余额
    public static final String BALANCE = "balance";
  }

  public static final class Tx {
    // 转账
    public static final String TRANSFER = "transfer";
    // 委托
    public static final String DELEGATE = "delegate";
  }
}
