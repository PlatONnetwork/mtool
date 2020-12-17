package com.platon.mtool.client.parser;

import com.beust.jcommander.JCommander;
import com.platon.mtool.client.AbstractOptionParser;
import com.platon.mtool.client.options.*;
import com.platon.mtool.client.options.restricting.CreateRestrictingPlanOption;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.AllCommands.Tx;
import com.platon.mtool.common.AllModules;

/** Created by liyf. */
public class BaseOptionParser extends AbstractOptionParser {
  private CreateRestrictingPlanOption createRestrictingPlanOption;
  private StakingOption stakingOption;
  private UpdateValidatorOption updateValidatorOption;
  private IncreaseStakingOption increaseStakingOption;
  private UnstakingOption unstakingOption;
  private DeclareVersionOption declareVersionOption;
  private SubmitVersionProposalOption submitVersionProposalOption;
  private SubmitCancelProposalOption submitCancelProposalOption;
  private SubmitTextProposalOption submitTextProposalOption;
  private SubmitParamProposalOption submitParamProposalOption;
  private VoteVersionProposalOption voteVersionProposalOption;
  private VoteCancelProposalOption voteCancelProposalOption;
  private VoteTextProposalOption voteTextProposalOption;
  private VoteParamProposalOption voteParamProposalOption;
  private CreateObserveWalletOption createObserveWalletOption;
  private OfflineSignOption offlineSignOption;
  private SendSignedTxOption sendSignedTxOption;
  private AccountOptions accountOptions;
  private TxOptions txOptions;

  public BaseOptionParser(String programName) {
    super(programName);
  }

  @Override
  public void buildOption(String programName, JCommander commander) {
    commander.setProgramName(programName);

    createRestrictingPlanOption = new CreateRestrictingPlanOption();
    commander.addCommand(createRestrictingPlanOption);

    stakingOption = new StakingOption();
    commander.addCommand(stakingOption);
    updateValidatorOption = new UpdateValidatorOption();
    commander.addCommand(updateValidatorOption);
    increaseStakingOption = new IncreaseStakingOption();
    commander.addCommand(increaseStakingOption);
    unstakingOption = new UnstakingOption();
    commander.addCommand(unstakingOption);
    submitVersionProposalOption = new SubmitVersionProposalOption();
    commander.addCommand(submitVersionProposalOption);
    voteVersionProposalOption = new VoteVersionProposalOption();
    commander.addCommand(voteVersionProposalOption);
    declareVersionOption = new DeclareVersionOption();
    commander.addCommand(declareVersionOption);
    submitCancelProposalOption = new SubmitCancelProposalOption();
    commander.addCommand(submitCancelProposalOption);
    submitTextProposalOption = new SubmitTextProposalOption();
    commander.addCommand(submitTextProposalOption);
    voteCancelProposalOption = new VoteCancelProposalOption();
    commander.addCommand(voteCancelProposalOption);
    voteTextProposalOption = new VoteTextProposalOption();
    commander.addCommand(voteTextProposalOption);
    createObserveWalletOption = new CreateObserveWalletOption();
    commander.addCommand(createObserveWalletOption);
    offlineSignOption = new OfflineSignOption();
    commander.addCommand(offlineSignOption);
    sendSignedTxOption = new SendSignedTxOption();
    commander.addCommand(sendSignedTxOption);
    submitParamProposalOption = new SubmitParamProposalOption();
    commander.addCommand(submitParamProposalOption);
    voteParamProposalOption = new VoteParamProposalOption();
    commander.addCommand(voteParamProposalOption);

    accountOptions = new AccountOptions();
    commander.addCommand(accountOptions);
    JCommander accountSubCommands = commander.getCommands().get(AllModules.ACCOUNT);
    accountSubCommands.addCommand(Account.NEW, accountOptions.getNewOption());
    accountSubCommands.addCommand(Account.RECOVER, accountOptions.getRecoverOption());
    accountSubCommands.addCommand(Account.LIST, accountOptions.getListOption());
    accountSubCommands.addCommand(Account.BALANCE, accountOptions.getBalanceOption());

    txOptions = new TxOptions();
    commander.addCommand(txOptions);
    JCommander txSubCommands = commander.getCommands().get(AllModules.TX);
    txSubCommands.addCommand(Tx.TRANSFER, txOptions.getTransferOption());
    txSubCommands.addCommand(Tx.DELEGATE, txOptions.getDelegateOption());
  }

  public CreateRestrictingPlanOption getCreateRestrictingPlanOption() {
    return createRestrictingPlanOption;
  }

  public StakingOption getStakingOption() {
    return stakingOption;
  }

  public UpdateValidatorOption getUpdateValidatorOption() {
    return updateValidatorOption;
  }

  public IncreaseStakingOption getIncreaseStakingOption() {
    return increaseStakingOption;
  }

  public UnstakingOption getUnstakingOption() {
    return unstakingOption;
  }

  public SubmitVersionProposalOption getSubmitVersionProposalOption() {
    return submitVersionProposalOption;
  }

  public VoteVersionProposalOption getVoteVersionProposalOption() {
    return voteVersionProposalOption;
  }

  public DeclareVersionOption getDeclareVersionOption() {
    return declareVersionOption;
  }

  public SubmitCancelProposalOption getSubmitCancelProposalOption() {
    return submitCancelProposalOption;
  }

  public SubmitTextProposalOption getSubmitTextProposalOption() {
    return submitTextProposalOption;
  }

  public VoteCancelProposalOption getVoteCancelProposalOption() {
    return voteCancelProposalOption;
  }

  public VoteTextProposalOption getVoteTextProposalOption() {
    return voteTextProposalOption;
  }

  public CreateObserveWalletOption getCreateObserveWalletOption() {
    return createObserveWalletOption;
  }

  public OfflineSignOption getOfflineSignOption() {
    return offlineSignOption;
  }

  public SendSignedTxOption getSendSignedTxOption() {
    return sendSignedTxOption;
  }

  public SubmitParamProposalOption getSubmitParamProposalOption() {
    return submitParamProposalOption;
  }

  public VoteParamProposalOption getVoteParamProposalOption() {
    return voteParamProposalOption;
  }

  public AccountOptions getAccountOptions() {
    return accountOptions;
  }

  public TxOptions getTxOptions() {
    return txOptions;
  }
}
