package com.platon.mtool.client.execute.sub;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.platon.crypto.Address;
import com.platon.crypto.WalletFile;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.AccountOptions.ListOption;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 钱包列表
 *
 * <p>Created by liyf
 */
public class AccountListExecutor extends MtoolExecutor<ListOption> {
  private static final Logger logger = LoggerFactory.getLogger(AccountListExecutor.class);

  public AccountListExecutor(JCommander commander, ListOption commonOption) {
    super(commander, commonOption);
  }

  @Override
  public void execute(ListOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg(Account.LIST).kv("option", option));

    List<Path> keystorePaths;
    try (Stream<Path> paths = Files.list(ResourceUtils.getKeystorePath())) {
      keystorePaths = paths.collect(Collectors.toList());
    }

    for (Path keystorePath : keystorePaths) {
      Address address = getAddress(keystorePath);
      if (address!=null) {
        PrintUtils.echo(
            "%s:\n" +
            " mainnet: %s\n" +
            " testnet: %s",
            FilenameUtils.getBaseName(keystorePath.getFileName().toString()),
                address.getMainnet(),
                address.getTestnet()
        );
      }
    }
  }

  private Address getAddress(Path keystorePath) {
    WalletFile walletFile;
    try {
      walletFile = JSON.parseObject(Files.newInputStream(keystorePath), WalletFile.class);
    } catch (Exception e) {
      LogUtils.info(
          logger, () -> Log.newBuilder().msg("ignore none json file").kv("name", keystorePath));
     walletFile = new WalletFile();
    }
    return walletFile.getAddress();
  }
}
