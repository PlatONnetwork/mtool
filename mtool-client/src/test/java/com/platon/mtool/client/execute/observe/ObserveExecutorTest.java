package com.platon.mtool.client.execute.observe;

import com.alibaba.fastjson.JSON;
import com.platon.contracts.ppos.dto.enums.VoteOption;
import com.platon.mtool.client.MtoolClient;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.client.options.*;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.ReflectionUtils;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.web3j.Keystore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;

/** Created by liyf. */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ObserveExecutorTest {

  private static final String WHITE_SPACE = "\\s+";
  private static Path resourceDirectory = Paths.get("src", "test", "resources");
  private static final Path OBSERVE_KEYSTORE_PATH =
      resourceDirectory.resolve("staking_observed.json");
  private static final Path VALIDATOR_CONFIG_PATH =
      resourceDirectory.resolve("validator_config.json");
  private static Keystore observedKeystore;
  private static ValidatorConfig validatorConfig;
  @Mock private BlockChainService blockChainService;

  private ValidatorConfig mockConfig() throws IOException {
    return JSON.parseObject(
            Files.newInputStream(resourceDirectory.resolve("validator_config.json")),
            ValidatorConfig.class);
  }
  @InjectMocks
  private ObserveVoteCancelProposalExecutor observeVoteCancelProposalExecutor =
      new ObserveVoteCancelProposalExecutor(null, null);

  @InjectMocks
  private ObserveVoteTextProposalExecutor observeVoteTextProposalExecutor =
      new ObserveVoteTextProposalExecutor(null, null);

  @InjectMocks
  private ObserveVoteVersionProposalExecutor observeVoteVersionProposalExecutor =
      new ObserveVoteVersionProposalExecutor(null, null);

  @InjectMocks
  private ObserveVoteParamProposalExecutor observeVoteParamProposalExecutor =
      new ObserveVoteParamProposalExecutor(null, null);

  @InjectMocks
  private ObserveSubmitCancelproposalExecutor observeSubmitCancelProposalExecutor =
      new ObserveSubmitCancelproposalExecutor(null, null);

  @InjectMocks
  private ObserveSubmitParamProposalExecutor observeSubmitParamProposalExecutor =
      new ObserveSubmitParamProposalExecutor(null, null);

  @BeforeAll
  static void beforeAll() {
    KeystoreConverter converter = new KeystoreConverter(AllParams.ADDRESS);
    observedKeystore = converter.convert(OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString());

    ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
    validatorConfig = configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
  }

  static Stream<String> cmdGenerator() {
    return Stream.of(
        String.format(
            "staking --amount 1000000 --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "unstaking --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "declare_version --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "increasestaking --amount 10 --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "submit_cancelproposal --proposalid 1abc --pid_id 1abc --end_voting_rounds 10 "
                + "--address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "submit_textproposal --pid_id 1abc --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "submit_versionproposal --end_voting_rounds 10 --newversion 1.0.0 --pid_id 1abc "
                + "--address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "submit_paramproposal --module module --paramname name --paramvalue value --pid_id 1abc"
                + " --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "update_validator --name asdf --url http://www.baidu.com --identity asdf --reward atp1vr8v48qjjrh9dwvdfctqauz98a7yp5sc4meext --introduction asdf --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "vote_cancelproposal --proposalid 1abc --opinion yes --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "vote_textproposal --proposalid 1abc --opinion yes --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "vote_versionproposal --proposalid 1abc --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
        String.format(
            "vote_paramproposal --proposalid 1abc --opinion yes --address %s --config %s",
            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()));
  }

  @BeforeEach
  void before() throws Exception {
    // blockChainService
    doNothing().when(blockChainService).validAddressNotSame(any(), any());
    doNothing().when(blockChainService).validAddressNotBeenUsed(any(), any(), any(), any());

    doNothing().when(blockChainService).validBalanceEnough(any(), any(), any(), any(), any());
    doNothing().when(blockChainService).validVoteProposal(any(), anyInt(), any());
    doNothing().when(blockChainService).validProposalExist(any(), any());
    doNothing().when(blockChainService).validCancelProposal(any(), any());
    doNothing().when(blockChainService).validGovernParam(any(), any(), any(), any());
    doNothing().when(blockChainService).validSelfStakingAddress(any(), any(), any());
  }

  @ParameterizedTest
  @MethodSource("cmdGenerator")
  void allCommand(String args) {
    try {
      new MtoolClient().run(args.split(WHITE_SPACE));
    } catch (Exception e) {
      Assertions.fail(e);
    }
    assertTrue(true);
  }

  @Test
  void unStaking() throws Exception {
    UnstakingOption option = new UnstakingOption();
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);

    ObserveUnstakingExecutor executor = new ObserveUnstakingExecutor(null, option);
    ReflectionUtils.setField(executor,"blockChainService",blockChainService,ObserveUnstakingExecutor.class);
    executor.execute(option);
    assertTrue(true);
  }

  @Test
  void declareVersion() throws Exception {
    DeclareVersionOption option = new DeclareVersionOption();
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    ObserveDeclareVersionExecutor executor = new ObserveDeclareVersionExecutor(null, option);
    ReflectionUtils.setField(executor,"blockChainService",blockChainService,ObserveDeclareVersionExecutor.class);
    executor.execute(option);
    assertTrue(true);
  }


  @Test
  void submitCancelProposal() throws Exception {
    SubmitCancelProposalOption option = new SubmitCancelProposalOption();
    option.setProposalid("proposalid");
    option.setPidId("pidId");
    option.setEndVotingRounds(BigInteger.valueOf(10));
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    observeSubmitCancelProposalExecutor.execute(option);
    assertTrue(true);
  }

  @Test
  void submitTextProposal() throws Exception {
    SubmitTextProposalOption option = new SubmitTextProposalOption();
    option.setPidId("pidId");
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    ObserveSubmitTextproposalExecutor executor =
        new ObserveSubmitTextproposalExecutor(null, option);
    ReflectionUtils.setField(executor,"blockChainService",blockChainService,ObserveSubmitTextproposalExecutor.class);
    executor.execute(option);
    assertTrue(true);
  }

  @Test
  void submitVersionProposal() throws Exception {
    SubmitVersionProposalOption option = new SubmitVersionProposalOption();
    option.setEndVotingRounds(BigInteger.valueOf(10));
    option.setNewversion(BigInteger.valueOf(100));
    option.setPidId("pidId");
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    ObserveSubmitVersionProposalExecutor executor =
        new ObserveSubmitVersionProposalExecutor(null, option);
    ReflectionUtils.setField(executor,"blockChainService",blockChainService,ObserveSubmitVersionProposalExecutor.class);
    executor.execute(option);
    assertTrue(true);
  }

  @Test
  void submitParamProposal() throws Exception {
    SubmitParamProposalOption option = new SubmitParamProposalOption();
    option.setModule("module");
    option.setParamName("name");
    option.setParamValue("value");
    option.setPidId("pidId");
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);

    observeSubmitParamProposalExecutor.execute(option);
    assertTrue(true);
  }

  @Test
  void updateValidator() throws Exception {
    UpdateValidatorOption option = new UpdateValidatorOption();
    option.setBenefitAddress("atp1vr8v48qjjrh9dwvdfctqauz98a7yp5sc4meext");
    option.setDetails("asdf");
    option.setExternalId("asdf");
    option.setNodeName("asdf");
    option.setWebsite("http://www.baidu.com");
    option.setDelegateRewardPercent(BigInteger.ONE);
    option.setConfig(mockConfig());
    option.setKeystore(observedKeystore);
    ObserveUpdateValidatorExecutor executor = new ObserveUpdateValidatorExecutor(null, option);
    ReflectionUtils.setField(executor,"blockChainService",blockChainService,ObserveUpdateValidatorExecutor.class);
    executor.execute(option);
    assertTrue(true);
  }

  @Test
  void voteCancelProposal() throws Exception {
    VoteCancelProposalOption option = new VoteCancelProposalOption();
    option.setProposalid("proposalid");
    option.setOpinion(VoteOption.NAYS);
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    observeVoteCancelProposalExecutor.execute(option);
    assertTrue(true);
  }

  @Test
  void voteTextProposal() throws Exception {
    VoteTextProposalOption option = new VoteTextProposalOption();
    option.setProposalid("proposalid");
    option.setOpinion(VoteOption.NAYS);
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    observeVoteTextProposalExecutor.execute(option);
    assertTrue(true);
  }

  @Test
  void voteVersionProposal() throws Exception {
    VoteVersionProposalOption option = new VoteVersionProposalOption();
    option.setProposalid("proposalid");
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    observeVoteVersionProposalExecutor.execute(option);
    assertTrue(true);
  }

  @Test
  void voteParamProposal() throws Exception {
    VoteParamProposalOption option = new VoteParamProposalOption();
    option.setProposalid("proposalid");
    option.setOpinion(VoteOption.NAYS);
    option.setConfig(validatorConfig);
    option.setKeystore(observedKeystore);
    observeVoteParamProposalExecutor.execute(option);
    assertTrue(true);
  }
}
