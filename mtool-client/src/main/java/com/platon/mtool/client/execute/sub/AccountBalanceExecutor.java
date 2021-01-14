package com.platon.mtool.client.execute.sub;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.platon.crypto.Address;
import com.platon.crypto.WalletFile;
import com.platon.crypto.WalletUtils;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.AccountOptions.BalanceOption;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.exception.MtoolClientExceptionCode;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.AddressUtil;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.DefaultBlockParameterName;
import com.platon.protocol.core.methods.response.PlatonGetBalance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 用户余额查询
 *
 * <p>Created by liyf
 */
public class AccountBalanceExecutor extends MtoolExecutor<BalanceOption> {
  private static final Logger logger = LoggerFactory.getLogger(AccountBalanceExecutor.class);

  public AccountBalanceExecutor(JCommander commander, BalanceOption commonOption) {
    super(commander, commonOption);
  }

  protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
    return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
  }

  @Override
  public void execute(BalanceOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg(Account.BALANCE).kv("option", option));

    String address;
    if (StringUtils.isNotEmpty(option.getAddress())) {
      address = option.getAddress();
    } else if (StringUtils.isNotEmpty(option.getKeystoreName())) {
      Path keystorePath = ResourceUtils.getKeystorePath().resolve(option.getKeystoreName());
      if (!keystorePath.toFile().exists()) {
        throw MtoolClientExceptionCode.FILE_NOT_FOUND.create();
      }
      Address addressBean = getAddress(keystorePath);
      if(CLIENT_CONFIG.getTargetChainId().equals(CLIENT_CONFIG.getMainNetChainId())){
        address = addressBean.getMainnet();
      }else{
        address = addressBean.getTestnet();
      }
    } else {
      throw MtoolClientExceptionCode.COMMAND_NOT_FOUND.create();
    }

    if (!WalletUtils.isValidAddress(address)) {
      throw new MtoolClientException("Invalid address");
    }
    if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),address)){
      throw new MtoolClientException("is not a legal address of chain["+CLIENT_CONFIG.getTargetChainId()+"]");
    }

    Web3j web3j = getWeb3j(option.getConfig());
    PlatonGetBalance platonGetBalance =
        web3j.platonGetBalance(address, DefaultBlockParameterName.LATEST).send();

    PrintUtils.echo("Balanceof: %s\nATP:%s", address, formatAmount(platonGetBalance.getBalance()));
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

  private static String formatAmount(BigInteger bigInteger) {
    return PlatOnUnit.vonToLat(bigInteger)
        .setScale(12, RoundingMode.DOWN)
        .stripTrailingZeros()
        .toPlainString();
  }
}
