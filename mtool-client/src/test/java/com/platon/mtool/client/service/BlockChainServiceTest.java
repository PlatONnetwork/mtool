package com.platon.mtool.client.service;

import com.alaya.contracts.ppos.ProposalContract;
import com.alaya.contracts.ppos.dto.common.ProposalType;
import com.alaya.contracts.ppos.dto.enums.GovernParamItemSupported;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.crypto.Address;
import com.alaya.crypto.Credentials;
import com.alaya.parameters.NetworkParameters;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonGetBalance;
import com.alaya.tx.gas.ContractGasProvider;
import com.alaya.tx.gas.DefaultGasProvider;
import com.alaya.tx.gas.GasProvider;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.client.test.MtoolParameterResolver;
import com.platon.mtool.client.test.PlatonMockHelp;
import com.platon.mtool.client.tools.ContractUtil;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.utils.AddressUtil;
import com.platon.mtool.common.utils.PlatOnUnit;
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

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;
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

  //@Mock
  private Web3j web3j;

  @Mock
  private ContractUtil contractUtil;

  @Mock
  private ProposalContract proposalContract;

  private ValidatorConfig config = null;
  private String proposalID = "0x00000000000000000000000000000000000000886d5ba2d3dfb2e2f6a1814f22";

  @BeforeEach
  public void setup(){
    // 把节点工具替换为mock对象
    //blockChainService.setNodeUtil(nodeUtil);
    ValidatorConfigConverter validatorConfigConverter = new ValidatorConfigConverter("config");

    //ValidatorConfig config = validatorConfigConverter.convert("D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator_config.json");

    try {
      config = validatorConfigConverter.convert(Paths.get(ClassLoader.getSystemResource("validator_config.json").toURI()).toString());
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(config);
  }

  @Test
  void validAddressNotSame(){
    Address address = new Address("atp1p27qmgmdps","atx1p27qzy6v5u");
    blockChainService.validAddressNotSame(address.getMainnet(), address.getTestnet());
    assertTrue(true);
  }

  @Test
  void validAddressNotSame_error(){
    Address address = new Address("atp1p27qmgmdps","atx1p27qzy6v5u");
    assertThrows(
            MtoolClientException.class,
            () -> blockChainService.validAddressNotSame(address.getMainnet(), address.getMainnet()));
    assertTrue(true);
  }

  @Test
  void validAddressNotBeenUsed(){
    Address address = new Address("atp1p27qmgmdps","atx1p27qzy6v5u");
    try {
      blockChainService.validAddressNotBeenUsed(web3j, address.getMainnet(), address.getMainnet(), config.getNodePublicKey());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void queryRestrictingBalance(){
    try {
      blockChainService.queryRestrictingBalance(config.getNodeAddress(), web3j);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void validProposalExist(){
    try {
      blockChainService.validProposalExist(web3j, proposalID);
    } catch (Exception e) {
      e.printStackTrace();
    }
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
    Address address = new Address("atp1qqqqqqqqqqqqqqqqqqqqqqqqqqqqqz4u3luxq6","atx1qqqqqqqqqqqqqqqqqqqqqqqqqqqqqz4umeqvns");
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
    Address address = new Address("atp1p27qmgmdps","atx1p27qzy6v5u");
    assertThrows(
        MtoolClientException.class,
        () -> blockChainService.validBalanceEnough(address, stakingAmount, gasProvider, web3j, StakingAmountType.RESTRICTING_AMOUNT_TYPE));
  }

  @Test
  void isSameAddress(ValidatorConfig validatorConfig, Credentials credentials) throws Exception {
    Web3j web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);

    Node node= new Node();
    node.setStakingAddress(credentials.getAddress(CLIENT_CONFIG.getTargetChainId()));
    when(contractUtil.getNode(any(),any())).thenReturn(node);
    boolean isSame =
        blockChainService.isSameAddress(web3j, validatorConfig.getNodePublicKey(), credentials.getAddress(CLIENT_CONFIG.getTargetChainId()));
    System.out.println(isSame);
    assertTrue(isSame);
  }

  @Test
  void validSelfStakingAddress(ValidatorConfig validatorConfig, Credentials credentials) {
    Web3j web3j = com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
    try {
      String mainNetAddress = credentials.getAddress(NetworkParameters.MainNetParams.getChainId());
      String testNetAddress = credentials.getAddress(NetworkParameters.TestNetParams.getChainId());
      Address address = new Address(mainNetAddress,testNetAddress);

      String targetChainAddress = AddressUtil.getTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),address.getMainnet());
      Node node= new Node();
      node.setStakingAddress(targetChainAddress);
      when(contractUtil.getNode(any(),any())).thenReturn(node);
      blockChainService.validSelfStakingAddress(web3j, validatorConfig.getNodePublicKey(), address);
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
