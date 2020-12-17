package com.platon.mtool.client.execute;

import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.options.VoteTextProposalOption;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.ProgressBar;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.alaya.contracts.ppos.ProposalContract;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.common.ProposalType;
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
 * 文本提案投票
 *
 * <p>Created by liyf.
 */
public class VoteTextProposalExecutor extends MtoolExecutor<VoteTextProposalOption> {

  private static final Logger logger = LoggerFactory.getLogger(VoteTextProposalExecutor.class);
  private BlockChainService blockChainService = BlockChainService.singleton();

  public VoteTextProposalExecutor(JCommander commander, VoteTextProposalOption commonOption) {
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
  public void execute(VoteTextProposalOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg("VoteText").kv("option", option));
    ProgressBar.start();
    ValidatorConfig validatorConfig = option.getConfig();
    Web3j web3j = getWeb3j(validatorConfig);

    blockChainService.validVoteProposal(web3j, ProposalType.TEXT_PROPOSAL, option.getProposalid());

    Credentials credentials = option.getKeystore().getCredentials();

    ProposalContract proposalContract =
        getProposalContract(web3j, credentials, CLIENT_CONFIG.getTargetChainId());
    ProgramVersion programVersion = web3j.getProgramVersion().send().getAdminProgramVersion();
    GasProvider gasProvider =
        proposalContract.getVoteProposalGasProvider(
            programVersion,
            option.getOpinion(),
            option.getProposalid(),
            validatorConfig.getNodePublicKey());
    blockChainService.validBalanceEnough(
        option.getKeystore().getAddress(), BigInteger.ZERO, gasProvider, web3j, StakingAmountType.FREE_AMOUNT_TYPE);
    PlatonSendTransaction transaction =
        proposalContract
            .voteReturnTransaction(
                programVersion,
                option.getProposalid(),
                validatorConfig.getNodePublicKey(),
                option.getOpinion(),
                gasProvider)
            .send();
    TransactionResponse response = proposalContract.getTransactionResponse(transaction).send();

    LogUtils.info(logger, () -> Log.newBuilder().msg("VoteText").kv("response", response));
    ProgressBar.stop();
    echoResult(
        transaction,
        response,
        validatorConfig.getNodePublicKey(),
        blockChainService.getCostAmount(null, gasProvider));
  }
}
