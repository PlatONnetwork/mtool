package com.platon.mtool.client.service;

import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.dto.common.ProposalType;
import com.platon.contracts.ppos.dto.enums.GovernParamItemSupported;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.crypto.Credentials;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.client.test.MtoolParameterResolver;
import com.platon.mtool.client.test.PlatonMockHelp;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.client.tools.ContractUtil;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.methods.response.PlatonGetBalance;
import com.platon.tx.gas.ContractGasProvider;
import com.platon.tx.gas.DefaultGasProvider;
import com.platon.tx.gas.GasProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/** Created by liyf. */
@ExtendWith(MtoolParameterResolver.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BlockChainServiceTest {

  @InjectMocks
  private BlockChainService blockChainService = BlockChainService.singleton();

  @Mock
  private Web3j web3j;

  @Mock
  private ContractUtil contractUtil;

  @Mock
  private ProposalContract proposalContract;

  private ValidatorConfig config = null;
  private String proposalID = "0x00000000000000000000000000000000000000886d5ba2d3dfb2e2f6a1814f22";
  private static String nodeAddress = "lat196278ns22j23awdfj9f2d4vz0pedld8anl5k3a";

  @BeforeEach
  public void setup(){
    CliConfigUtils.loadProperties();

    // 把节点工具替换为mock对象
    //blockChainService.setNodeUtil(nodeUtil);
    ValidatorConfigConverter validatorConfigConverter = new ValidatorConfigConverter("config");

    //ValidatorConfig config = validatorConfigConverter.convert("D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator_config.json");

    try {
      config = validatorConfigConverter.convert(Paths.get(ClassLoader.getSystemResource("validator_config.json").toURI()).toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    //web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(config);
  }


  @Test
  void validAddressNotSame_error(){
    String address = "lat1p27qmgmdps";

    assertThrows(
            MtoolClientException.class,
            () -> blockChainService.validAddressNotSame(address, address));
    assertTrue(true);
  }

  @Test
  void validAddressNotBeenUsed(){
    String address = "lat1p27qmgmdps";

    try {
      blockChainService.validAddressNotBeenUsed(web3j, address, address, config.getNodePublicKey());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void queryRestrictingBalance(){
    try {
      blockChainService.queryRestrictingBalanceAvailableForStakingOrDelegation(nodeAddress, web3j);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void validProposalExist(){

    MtoolClientException exception =
            assertThrows(MtoolClientException.class, () -> blockChainService.validProposalExist(web3j, proposalID));
    assertEquals("proposal not found", exception.getMessage());
  }

  @Test
  void validVoteProposal(){
    assertThrows(
            MtoolClientException.class,
            () -> blockChainService.validVoteProposal(web3j, ProposalType.TEXT_PROPOSAL, proposalID));
  }

  @Test
  void validBalanceEnough() throws Exception {
    // given
    BigInteger stakingAmount = PlatOnUnit.latToVon(BigInteger.valueOf(5000_000));

    PlatonGetBalance balanceReponse = new PlatonGetBalance();
    balanceReponse.setResult("0x" + stakingAmount.toString(16));
    PlatonMockHelp.mockWeb3j(web3j, balanceReponse)
        .platonGetBalance(Mockito.anyString(), any());

    GasProvider gasProvider = new DefaultGasProvider();
    BigInteger enoughAmount =
        stakingAmount.add(gasProvider.getGasLimit().multiply(gasProvider.getGasPrice()));
    String address = "lat1qqqqqqqqqqqqqqqqqqqqqqqqqqqqqz4ugf27l4";
    assertThrows(
        MtoolClientException.class,
        () -> blockChainService.validBalanceEnough(address, stakingAmount, gasProvider, web3j, StakingAmountType.FREE_AMOUNT_TYPE));
    assertTrue(true);
  }

  @Test
  void balanceNotEnough() throws Exception {
    // given
    BigInteger stakingAmount = PlatOnUnit.latToVon(BigInteger.valueOf(5000_000));

    PlatonGetBalance balanceReponse = new PlatonGetBalance();
    balanceReponse.setResult("0x" + stakingAmount.toString(16));
    PlatonMockHelp.mockWeb3j(web3j, balanceReponse)
        .platonGetBalance(Mockito.anyString(), any());

    GasProvider gasProvider = new DefaultGasProvider();
    String address = "lat1p27qmgmdps";
    assertThrows(
        MtoolClientException.class,
        () -> blockChainService.validBalanceEnough(address, stakingAmount, gasProvider, web3j, StakingAmountType.RESTRICTING_AMOUNT_TYPE));
  }

  @Test
  void isSameAddress(ValidatorConfig validatorConfig, Credentials credentials) throws Exception {
    Web3j web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);

    Node node= new Node();
    node.setStakingAddress(credentials.getAddress());
    when(contractUtil.getNode(any(),any())).thenReturn(node);
    boolean isSame =
        blockChainService.isSameAddress(web3j, validatorConfig.getNodePublicKey(), credentials.getAddress());
    System.out.println(isSame);
    assertTrue(isSame);
  }

  @Test
  void validSelfStakingAddress(ValidatorConfig validatorConfig, Credentials credentials) {
    Web3j web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
    try {
      String mainNetAddress = credentials.getAddress();

      Node node= new Node();
      node.setStakingAddress(mainNetAddress);
      when(contractUtil.getNode(any(),any())).thenReturn(node);
      blockChainService.validSelfStakingAddress(web3j, validatorConfig.getNodePublicKey(), mainNetAddress);
    } catch (Exception e) {
      fail();
    }
    assertTrue(true);
  }

  @Test
  void singleton() {
    BlockChainService b = BlockChainService.singleton();
    assertSame(blockChainService, b);
  }

  @Test
  void getCostAmount() {
    GasProvider provider = new ContractGasProvider(BigInteger.valueOf(2), BigInteger.valueOf(3));
    BigInteger costAmount = blockChainService.getCostAmount(BigInteger.ONE, provider);
    assertEquals(BigInteger.valueOf(7), costAmount);
    costAmount = blockChainService.getCostAmount(null, provider);
    assertEquals(BigInteger.valueOf(6), costAmount);
  }

  @Test
  void getGovernParam(){
    try {
      String minimum = blockChainService.getGovernParam(web3j, GovernParamItemSupported.Staking_stakeThreshold.getModule(), GovernParamItemSupported.Staking_stakeThreshold.getName());
      System.out.println("minimum:" + minimum);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void getGovernParamValue(){
    try {
      String minimum = blockChainService.getGovernParamValue(web3j, GovernParamItemSupported.Restricting_minimumRelease);
      System.out.println("minimum:" + minimum);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
