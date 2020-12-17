package com.platon.mtool.client.execute;

import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.options.DeclareVersionOption;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.ProgressBar;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.alaya.contracts.ppos.ProposalContract;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.core.methods.response.bean.ProgramVersion;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.tx.gas.GasProvider;

import java.math.BigInteger;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 声明版本提案
 *
 * <p>Created by liyf.
 */
public class DeclareVersionExecutor extends MtoolExecutor<DeclareVersionOption> {

  private static final Logger logger = LoggerFactory.getLogger(DeclareVersionExecutor.class);
  private BlockChainService blockChainService = BlockChainService.singleton();

  public DeclareVersionExecutor(JCommander commander, DeclareVersionOption commonOption) {
    super(commander, commonOption);
  }

  protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
    return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
  }

  protected ProposalContract getProposalContract(
      Web3j web3j, Credentials credentials, Long chainId) {
    return ProposalContract.load(web3j, credentials, chainId);
  }

  @Override
  public void execute(DeclareVersionOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg("DeclareVersion").kv("option", option));
    ProgressBar.start();
    ValidatorConfig validatorConfig = option.getConfig();

    Web3j web3j = getWeb3j(validatorConfig);
    Credentials credentials = option.getKeystore().getCredentials();

    ProposalContract proposalContract =
        getProposalContract(web3j, credentials, CLIENT_CONFIG.getTargetChainId());
    ProgramVersion programVersion = web3j.getProgramVersion().send().getAdminProgramVersion();
    GasProvider gasProvider =
        proposalContract.getDeclareVersionGasProvider(
            programVersion, validatorConfig.getNodePublicKey());
    blockChainService.validBalanceEnough(
        option.getKeystore().getAddress(), BigInteger.ZERO, gasProvider, web3j, StakingAmountType.FREE_AMOUNT_TYPE);
    PlatonSendTransaction transaction =
        proposalContract
            .declareVersionReturnTransaction(
                programVersion, validatorConfig.getNodePublicKey(), gasProvider)
            .send();
    TransactionResponse response = proposalContract.getTransactionResponse(transaction).send();

    LogUtils.info(logger, () -> Log.newBuilder().msg("DeclareVersion").kv("response", response));
    ProgressBar.stop();
    echoResult(
        transaction,
        response,
        validatorConfig.getNodePublicKey(),
        blockChainService.getCostAmount(null, gasProvider));
  }
}
