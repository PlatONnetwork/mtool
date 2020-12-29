package com.platon.mtool.client.service;

import com.alaya.contracts.ppos.NodeContract;
import com.alaya.contracts.ppos.ProposalContract;
import com.alaya.contracts.ppos.RestrictingPlanContract;
import com.alaya.contracts.ppos.StakingContract;
import com.alaya.contracts.ppos.dto.CallResponse;
import com.alaya.contracts.ppos.dto.common.ProposalType;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.contracts.ppos.dto.resp.ParamItem;
import com.alaya.contracts.ppos.dto.resp.Proposal;
import com.alaya.contracts.ppos.dto.resp.RestrictingItem;
import com.alaya.crypto.Address;
import com.alaya.crypto.WalletUtils;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.DefaultBlockParameterName;
import com.alaya.tx.gas.GasProvider;
import com.platon.mtool.client.tools.ContractUtil;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.exception.MtoolClientExceptionCode;
import com.platon.mtool.common.exception.MtoolPlatonExceptionCode;
import com.platon.mtool.common.utils.AddressUtil;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.mtool.common.validate.ParamValidator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 区块链交互方法封装，单例
 *
 * <p>Created by liyf.
 */
public class BlockChainService {

  private ContractUtil contractUtil =new ContractUtil();
  public void setContractUtil(ContractUtil contractUtil){this.contractUtil = contractUtil;}

  private BlockChainService() {}

  public static BlockChainService singleton() {
    return BlockChainServiceHolder.instance;
  }

  public void validAddressNotSame(String stakingAddress, String benefitAddress) {
    if (stakingAddress.equalsIgnoreCase(benefitAddress)) {
      throw new MtoolClientException(
          "The benefitAddress cannot be the same as the Staking wallet address.");
    }
  }

  public void validAddressNotBeenUsed(
      Web3j web3j, String stakingAddress, String benefitAddress, String nodeId) throws Exception {
    NodeContract nodeContract = NodeContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<List<Node>> baseResponse = nodeContract.getCandidateList().send();
    for (Node node : baseResponse.getData()) {
      if (node.getNodeId().equals(nodeId)) {
        continue;
      }
      if (node.getStakingAddress().equalsIgnoreCase(stakingAddress)
          || node.getBenifitAddress().equalsIgnoreCase(stakingAddress)) {
        throw new MtoolClientException(
            "The Staking wallet cannot be the same as the wallet address associated with other validators");
      }
      if (node.getStakingAddress().equalsIgnoreCase(benefitAddress)
          || node.getBenifitAddress().equalsIgnoreCase(benefitAddress)) {
        throw new MtoolClientException(
            "The 'benefitAddress' cannot be the same as the wallet address associated with other validators");
      }
    }
  }

  /**
   * 用户余额是否足够支付交易.
   *
   * @param address 钱包地址
   * @param amount 交易金额
   * @param gasProvider 手续费工具
   * @param web3j web3j连接
   * @throws IOException 底层出错
   */
  public void validBalanceEnough(
          Address address, BigInteger amount, GasProvider gasProvider, Web3j web3j, StakingAmountType amountType) throws Exception {
    // 总花费 = 交易金额 + 手续费
    BigInteger total = getCostAmount(amount, gasProvider);
    BigInteger fee = total.subtract(amount);

    String targetChainAddress = AddressUtil.getTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),address.getMainnet());
    // 钱包自由余额
    BigInteger freeBalance = web3j.platonGetBalance(targetChainAddress, DefaultBlockParameterName.LATEST).send().getBalance();
    switch (amountType){
      case FREE_AMOUNT_TYPE:
        if (freeBalance.compareTo(total) < 0) {
          throw MtoolClientExceptionCode.BALANCE_NO_ENOUGH.create(PlatOnUnit.vonToLat(total));
        }
        break;
      case RESTRICTING_AMOUNT_TYPE:
        // 钱包锁仓可用余额
        BigInteger restrictBalance = queryRestrictingBalanceAvailableForStakingOrDelegation(targetChainAddress,web3j);
        if(restrictBalance.compareTo(amount)<0){
          throw MtoolClientExceptionCode.BALANCE_NO_ENOUGH.create(PlatOnUnit.vonToLat(total));
        }
        if (freeBalance.compareTo(fee) < 0) {
          System.out.printf("The wallet balance(%.8f) is insufficient to pay the transaction fee(%.8f).", PlatOnUnit.vonToLat(freeBalance), PlatOnUnit.vonToLat(fee));
          throw MtoolClientExceptionCode.BALANCE_NO_ENOUGH.create(PlatOnUnit.vonToLat(total));
        }
        break;
      case AUTO_AMOUNT_TYPE:
        // 钱包锁仓可用余额
        restrictBalance = queryRestrictingBalanceAvailableForStakingOrDelegation(targetChainAddress,web3j);
        BigInteger totalBalance = restrictBalance.add(freeBalance);

        if(totalBalance.compareTo(total)<0){
          throw MtoolClientExceptionCode.BALANCE_NO_ENOUGH.create(PlatOnUnit.vonToLat(total));
        }
        if (freeBalance.compareTo(fee) < 0) {
          System.out.printf("The wallet balance(%.8f) is insufficient to pay the transaction fee(%.8f).", PlatOnUnit.vonToLat(freeBalance), PlatOnUnit.vonToLat(fee));
          throw MtoolClientExceptionCode.BALANCE_NO_ENOUGH.create(PlatOnUnit.vonToLat(total));
        }
        break;
    }
  }

  /**
   * 取用户的锁仓余额，这个余额是指，可以用来继续做锁仓，或者委托的金额，所以，需要balance-pledge;
   * @param userAddress
   * @param web3j
   * @return
   * @throws Exception
   */
  public BigInteger queryRestrictingBalanceAvailableForStakingOrDelegation(String userAddress, Web3j web3j) throws Exception {
    BigInteger available = BigInteger.ZERO;
    RestrictingPlanContract restrictingPlanContract = RestrictingPlanContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<RestrictingItem> baseResponse = restrictingPlanContract.getRestrictingInfo(userAddress).send();
    if(baseResponse.getCode() == 0){
      RestrictingItem restrictingItem = baseResponse.getData();
      BigInteger sub = restrictingItem.getBalance().subtract(restrictingItem.getPledge());
      if (sub.signum()==1){//可用余额>0才返回，否则返回0
        available = sub;
      }
    }else{
      System.out.println(baseResponse.getErrMsg());
    }
    return available;
  }

  public BigInteger getCostAmount(BigInteger amount, GasProvider gasProvider) {
    if (amount == null) {
      return gasProvider.getGasLimit().multiply(gasProvider.getGasPrice()).add(BigInteger.ZERO);
    } else {
      return gasProvider.getGasLimit().multiply(gasProvider.getGasPrice()).add(amount);
    }
  }

  /**
   * 判断--address输入的钱包与交易的钱包是否一致
   *
   * @param web3j
   * @param nodeId
   * @param addr
   * @return
   * @throws Exception
   */
  public boolean isSameAddress(Web3j web3j, String nodeId, String addr) throws Exception {
    Node node = contractUtil.getNode(web3j,nodeId);
    if (node == null) {
      return true;
    } else {
      return node.getStakingAddress().equalsIgnoreCase(addr);
    }
  }

  /**
   * 节点信息获取的地址必须与质押地址一致
   *
   * @param web3j
   * @param nodeId
   * @param address
   * @throws Exception
   */
  public void validSelfStakingAddress(Web3j web3j, String nodeId, Address address) throws Exception {
    String targetChainAddress = AddressUtil.getTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),address.getMainnet());
    Node node = contractUtil.getNode(web3j,nodeId);
    if (node != null && !node.getStakingAddress().equalsIgnoreCase(targetChainAddress)) {
      throw new MtoolClientException("address mismatch");
    }
  }

  /**
   * 查询提案是否存在.
   *
   * @param web3j
   * @param proposalid
   * @return
   * @throws Exception
   */
  public void validProposalExist(Web3j web3j, String proposalid) throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<Proposal> response = proposalContract.getProposal(proposalid).send();
    if (response.getData() == null) {
      throw MtoolClientExceptionCode.PROPOSAL_NO_FOUND.create();
    }
  }

  public void validVoteProposal(Web3j web3j, int proposalType, String proposalId) throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<Proposal> response = proposalContract.getProposal(proposalId).send();
    if (response.getData() == null) {
      throw MtoolClientExceptionCode.PROPOSAL_NO_FOUND.create();
    }
    if (response.getData().getProposalType() != proposalType) {
      throw MtoolClientExceptionCode.getFromCode(
              MtoolClientException.PROPOSAL_PREFIX + proposalType)
          .create();
    }
    // 由于该接口无法返回 投票中状态， 所以去掉该校验
    /*CallResponse<TallyResult> result = proposalContract.getTallyResult(proposalId).send();
    if (result.getData() != null && result.getData().getStatus() != 1) {
      throw MtoolClientExceptionCode.PROPOSAL_CANT_VOTE.create();
    }*/
  }

  /** 校验取消提案. */
  public void validCancelProposal(Web3j web3j, String proposalId) throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<Proposal> response = proposalContract.getProposal(proposalId).send();
    if (response.getData() == null) {
      throw MtoolClientExceptionCode.PROPOSAL_NO_FOUND.create();
    }
    if (!Arrays.asList(ProposalType.VERSION_PROPOSAL, ProposalType.PARAM_PROPOSAL)
        .contains(response.getData().getProposalType())) {
      throw MtoolClientExceptionCode.CANCEL_PROPOSAL_NOT_SUPPORT.create();
    }
  }

  /** 校验参数提案模块和参数 */
  public void validGovernParam(Web3j web3j, String module, String paramName, String paramValue)
      throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<String> response = proposalContract.getGovernParamValue(module, paramName).send();
    if (response == null) {
      throw new MtoolClientException("unknow platon error");
    }
    if (!response.isStatusOk()) {
      throw MtoolPlatonExceptionCode.getFromCode(response.getCode()).create();
    }
    if (response.getData().equals(paramValue)) {
      throw MtoolClientExceptionCode.PARAM_VALUE_NOT_BE_SAME.create();
    }
  }

  /** 校验参数提案模块和参数 */
  public String getGovernParamValue(Web3j web3j, ParamItem paramItem) throws Exception{
      return getGovernParam(web3j, paramItem.getModule(), paramItem.getName());
  }


  /** 校验参数提案模块和参数 */
  public String getGovernParam(Web3j web3j, String module, String paramName)
          throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<String> response = proposalContract.getGovernParamValue(module, paramName).send();
    if (response == null) {
      throw new MtoolClientException("unknow platon error");
    }
    if (!response.isStatusOk()) {
      throw MtoolPlatonExceptionCode.getFromCode(response.getCode()).create();
    }
    return response.getData();
  }

  private static class BlockChainServiceHolder {
    private static final BlockChainService instance = new BlockChainService();
  }

  public void validConfig(ValidatorConfig validatorConfig, Class<?>... groups) {
    Map<String, String> invalidParamMap = ParamValidator.validConfig(validatorConfig, groups);
    if (!invalidParamMap.isEmpty()) {
      for (Map.Entry<String, String> entry : invalidParamMap.entrySet()) {
        PrintUtils.echo(entry.getKey() + " " + entry.getValue());
      }
      throw new MtoolClientException("validator param is invalid");
    }
  }

  public void validPrivateKey(String privateKey) {
    if (StringUtils.isEmpty(privateKey)) {
      throw new MtoolClientException("Private Key can't be empty");
    }
    if (!WalletUtils.isValidPrivateKey(privateKey)) {
      throw new MtoolClientException("Private key cannot be parsed");
    }
  }

  /**
   * 判断节点id是否存在
   *
   * @param web3j
   * @param nodeId
   * @throws Exception
   */
  public void validNodeExist(Web3j web3j, String nodeId) throws Exception {
    StakingContract stakingContract = StakingContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    Node node = stakingContract.getStakingInfo(nodeId).send().getData();
    if (node == null || StringUtils.isEmpty(node.getNodeId())) {
      throw new MtoolClientException("This node (" + nodeId + ") does not exist");
    }
  }

  /**
   * 校验金额是否满足最小值要求
   * @throws Exception
   */
  public void validAmount(Web3j web3j, String module, String paramName, BigInteger amount)
          throws Exception {
    ProposalContract proposalContract = ProposalContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
    CallResponse<String> response = proposalContract.getGovernParamValue(module, paramName).send();
    if (response == null) {
      throw new MtoolClientException("unknow platon error");
    }
    if(!response.isStatusOk()){
      throw new MtoolClientException("Request target chain["+CLIENT_CONFIG.getTargetChainId()+"] error");
    }
    String amountStr = response.getData();
    if (StringUtils.isNotEmpty(amountStr)) {
      BigInteger minAmount = new BigInteger(amountStr);
      if(minAmount.compareTo(amount)>0){
        BigDecimal minAmountLat = PlatOnUnit.vonToLat(minAmount);
        throw new MtoolClientException("Amount cannot be less than "+minAmountLat.intValue()+" ATP");
      }
    }else{
      throw new MtoolClientException("Response error");
    }
  }
}
