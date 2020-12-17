package com.platon.mtool.client.router;

import com.beust.jcommander.JCommander;
import com.platon.mtool.client.AbstractOptionParser;
import com.platon.mtool.client.CliExecutor;
import com.platon.mtool.client.execute.sub.TxDelegateExecutor;
import com.platon.mtool.client.execute.sub.TxTransferExecutor;
import com.platon.mtool.client.options.TxOptions;
import com.platon.mtool.common.AllCommands.Tx;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * tx模块命令路由器
 *
 * <p>Created by liyf
 */
public class TxRouter extends CliExecutor<TxOptions> {
  private static final Logger logger = LoggerFactory.getLogger(TxRouter.class);

  public TxRouter(JCommander commander, TxOptions commonOption) {
    super(commander, commonOption);
  }

  @Override
  public void execute(TxOptions option) throws Exception {
    JCommander subCommander = AbstractOptionParser.getSubCommander(getCommander());
    String subCommandName = AbstractOptionParser.getCommandName(getCommander());
    LogUtils.info(logger, () -> Log.newBuilder().kv("subCommandName", subCommandName));
    CliExecutor<?> executor;
    switch (subCommandName) {
      case Tx.TRANSFER:
        executor = new TxTransferExecutor(subCommander, getCommonOption().getTransferOption());
        break;
      case Tx.DELEGATE:
        executor = new TxDelegateExecutor(subCommander, getCommonOption().getDelegateOption());
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
