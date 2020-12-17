package com.platon.mtool.client.router;

import com.beust.jcommander.JCommander;
import com.platon.mtool.client.AbstractOptionParser;
import com.platon.mtool.client.CliExecutor;
import com.platon.mtool.client.execute.sub.AccountBalanceExecutor;
import com.platon.mtool.client.execute.sub.AccountListExecutor;
import com.platon.mtool.client.execute.sub.AccountNewExecutor;
import com.platon.mtool.client.execute.sub.AccountRecoverExecutor;
import com.platon.mtool.client.options.AccountOptions;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by liyf */
public class AccountRouter extends CliExecutor<AccountOptions> {
  private static final Logger logger = LoggerFactory.getLogger(AccountRouter.class);

  public AccountRouter(JCommander commander, AccountOptions commonOption) {
    super(commander, commonOption);
  }

  @Override
  public void execute(AccountOptions option) throws Exception {
    JCommander subCommander = AbstractOptionParser.getSubCommander(getCommander());
    String subCommandName = AbstractOptionParser.getCommandName(getCommander());
    LogUtils.info(logger, () -> Log.newBuilder().kv("subCommandName", subCommandName));
    CliExecutor<?> executor;
    switch (subCommandName) {
      case Account.NEW:
        executor = new AccountNewExecutor(subCommander, getCommonOption().getNewOption());
        break;
      case Account.RECOVER:
        executor = new AccountRecoverExecutor(subCommander, getCommonOption().getRecoverOption());
        break;
      case Account.LIST:
        executor = new AccountListExecutor(subCommander, getCommonOption().getListOption());
        break;
      case Account.BALANCE:
        executor = new AccountBalanceExecutor(subCommander, getCommonOption().getBalanceOption());
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + subCommandName);
    }
    executor.execute();
  }

  @Override
  protected void handleException(Exception e) {
    help();
  }
}
