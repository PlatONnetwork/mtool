package com.platon.mtool.client.execute;

import com.alibaba.fastjson.JSON;
import com.platon.contracts.ppos.DelegateContract;
import com.platon.contracts.ppos.ProposalContract;
import com.platon.contracts.ppos.RestrictingPlanContract;
import com.platon.contracts.ppos.StakingContract;
import com.platon.contracts.ppos.dto.TransactionResponse;
import com.platon.contracts.ppos.dto.enums.VoteOption;
import com.platon.contracts.ppos.dto.resp.Node;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import com.platon.mtool.client.MtoolClient;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.client.converter.StakingAmountConverter;
import com.platon.mtool.client.converter.ValidatorConfigConverter;
import com.platon.mtool.client.execute.restricting.CreateRestrictingPlanExecutor;
import com.platon.mtool.client.execute.sub.TxDelegateExecutor;
import com.platon.mtool.client.execute.sub.TxTransferExecutor;
import com.platon.mtool.client.options.*;
import com.platon.mtool.client.options.restricting.CreateRestrictingPlanOption;
import com.platon.mtool.client.options.restricting.RestrictingConfig;
import com.platon.mtool.client.options.restricting.RestrictingConfigConverter;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.test.PlatonJsonHelp;
import com.platon.mtool.client.test.PlatonMockHelp;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.web3j.EmptyContract;
import com.platon.mtool.common.web3j.Keystore;
import com.platon.mtool.common.web3j.TransactionEntity;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.methods.response.PlatonGetTransactionReceipt;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.tx.Transfer;
import com.platon.tx.gas.DefaultGasProvider;
import com.platon.tx.gas.GasProvider;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by liyf.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExecutorTest {

    private static final String WHITE_SPACE = "\\s+";
    private static Path resourceDirectory = Paths.get("src", "test", "resources");
    private static final Path STAKING_KEYSTORE_PATH = resourceDirectory.resolve("staking.keystore");
    private static final Path STAKING_KEYSTORE_PATH_ERROR = resourceDirectory.resolve("staking_error.keystore");
    private static final Path VALIDATOR_CONFIG_PATH =
            resourceDirectory.resolve("validator_config.json");
    private static final Path TRANSACTION_DETAIL_PATH =
            resourceDirectory.resolve("transaction_detail.csv");
    private static final Path RESTRICTING_PLAN_PATH =
            resourceDirectory.resolve("restricting_plans.json");
    private static final Path RESTRICTING_PLAN_PATH_ERROR =
            resourceDirectory.resolve("restricting_plans_error.json");
    private static final Path RESTRICTING_PLAN_PATH_36 =
            resourceDirectory.resolve("restricting_plans_36.json");
    private static Keystore observedKeystore;
    private static final Path OBSERVE_KEYSTORE_PATH =
            resourceDirectory.resolve("staking_observed.json");

    @Mock
    private Web3j web3j;
    @Mock
    private BlockChainService blockChainService;
    @Mock
    private RestrictingPlanContract restrictingPlanContract;
    @Mock
    private StakingContract stakingContract;
    @Mock
    private ProposalContract proposalContract;
    @Mock
    private DelegateContract delegateContract;
    @Mock
    private EmptyContract emptyContract;
    @Mock
    private Transfer transfer;

    private ValidatorConfig mockConfig() throws IOException {
        return JSON.parseObject(
                Files.newInputStream(resourceDirectory.resolve("validator_config.json")),
                ValidatorConfig.class);
    }

    @InjectMocks
    private CreateRestrictingPlanExecutor createRestrictingPlanExecutor = new CreateRestrictingPlanExecutor(null, null);

    @InjectMocks
    private StakingExecutor stakingExecutor = new StakingExecutor(null, null);

    @InjectMocks
    private IncreaseStakingExecutor increaseStakingExecutor = new IncreaseStakingExecutor(null, null);

    @InjectMocks
    private UpdateValidatorExecutor updateValidatorExecutor = new UpdateValidatorExecutor(null, null);

    @InjectMocks
    private VoteTextProposalExecutor voteTextProposalExecutor =
            new VoteTextProposalExecutor(null, null);

    @InjectMocks
    private VoteVersionProposalExecutor voteVersionProposalExecutor =
            new VoteVersionProposalExecutor(null, null);

    @InjectMocks
    private VoteCancelProposalExecutor voteCancelProposalExecutor =
            new VoteCancelProposalExecutor(null, null);

    @InjectMocks
    private VoteParamProposalExecutor voteParamProposalExecutor =
            new VoteParamProposalExecutor(null, null);

    @InjectMocks
    private DeclareVersionExecutor declareVersionExecutor = new DeclareVersionExecutor(null, null);

    @InjectMocks
    private SubmitCancelProposalExecutor submitCancelProposalExecutor =
            new SubmitCancelProposalExecutor(null, null);

    @InjectMocks
    private SubmitParamProposalExecutor submitParamProposalExecutor =
            new SubmitParamProposalExecutor(null, null);

    @InjectMocks
    private UnstakingExecutor unstakingExecutor = new UnstakingExecutor(null, null);

    @InjectMocks
    private SubmitTextProposalExecutor submitTextProposalExecutor =
            new SubmitTextProposalExecutor(null, null);

    @InjectMocks
    private SubmitVersionProposalExecutor submitVersionProposalExecutor =
            new SubmitVersionProposalExecutor(null, null);

    @InjectMocks
    private SendSignedTxExecutor sendSignedTxExecutor = new SendSignedTxExecutor(null, null);

    @InjectMocks
    private TxDelegateExecutor txDelegateExecutor = new TxDelegateExecutor(null, null);

    @InjectMocks
    private TxTransferExecutor txTransferExecutor = new TxTransferExecutor(null, null);

    private PlatonSendTransaction transaction;
    private TransactionResponse successResponse = new TransactionResponse();

    private static String benefitAddress = "lat12jn6835z96ez93flwezrwu4xpv8e4zathsyxdn";
    private static String nodeName = "nodeNameForTest";
    private static String website = "www.website.com";
    private static String externalId = "github_commitID";
    private static String details = "node_description";

    ExecutorTest() {
        successResponse.setCode(0);
    }

    static Stream<String> cmdGenerator() {
        return Stream.of(
                String.format(
                        "account list"),

                String.format(
                        "create_restricting --keystore %s --config %s --file %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString(),
                        RESTRICTING_PLAN_PATH.toAbsolutePath().toString()),
                String.format(
                        "staking --autoamount 1000000 --keystore %s --config %s --benefit_address %s  --delegated_reward_rate 5000 --node_name %s --external_id %s --website %s --details %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString(),
                        benefitAddress, nodeName, externalId, website, details),

                String.format(
                        "staking --amount 1000000 --keystore %s --config %s --benefit_address %s  --delegated_reward_rate 5000 --node_name %s --external_id %s --website %s --details %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString(),
                        benefitAddress, nodeName, externalId, website, details),
                String.format(
                        "unstaking --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "declare_version --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "increasestaking --amount 10 --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "submit_cancelproposal --proposalid 1abc --pid_id 1abc --end_voting_rounds 10"
                                + " --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "submit_textproposal --pid_id 1abc --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "submit_versionproposal --end_voting_rounds 10 --newversion 1.0.0 --pid_id 1abc"
                                + " --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "submit_paramproposal --module module --paramname name --paramvalue value --pid_id 1abc"
                                + " --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "update_validator --node_name new_name --website new_website --delegated_reward_rate 6000 --benefit_address lat1vr8v48qjjrh9dwvdfctqauz98a7yp5scvd0pey"
                                + " --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "vote_cancelproposal --proposalid 1abc --opinion yes --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "vote_textproposal --proposalid 1abc --opinion yes --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "vote_versionproposal --proposalid 1abc --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format(
                        "vote_paramproposal --proposalid 1abc --opinion yes --keystore %s --config %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
                String.format("offlinesign --filelocation %s", TRANSACTION_DETAIL_PATH.toAbsolutePath().toString()),
                String.format(
                        "send_signedtx --filelocation %s --config %s",
                        TRANSACTION_DETAIL_PATH.toAbsolutePath().toString(),
                        VALIDATOR_CONFIG_PATH.toAbsolutePath().toString())
        );
    }

    /*@ParameterizedTest
    @MethodSource("cmdGenerator")
    void allCommand(String args) {
        try {
            new MtoolClient().run(args.split(WHITE_SPACE));
        } catch (Exception e) {
            Assertions.fail(e);
        }
        assertTrue(true);
    }*/

    @BeforeAll
    static void beforeAll() {

        CliConfigUtils.loadProperties();

        KeystoreConverter converter = new KeystoreConverter(AllParams.ADDRESS);
        observedKeystore = converter.convert(OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString());

    }

    @BeforeEach
    void before() throws Exception {
        transaction = new PlatonSendTransaction();
        transaction.setResult("0xf14f74386e6ef9027c48582d7faed3b50ab1ffdd047d6ba3afcf27791afb4e9b");

        // blockChainService
        doNothing().when(blockChainService).validAddressNotSame(any(), any());
        doNothing().when(blockChainService).validAddressNotBeenUsed(any(), any(), any(), any());

        doNothing().when(blockChainService).validBalanceEnough(any(), any(), any(), any(), any());
        doNothing().when(blockChainService).validVoteProposal(any(), anyInt(), any());
        doNothing().when(blockChainService).validProposalExist(any(), any());
        doNothing().when(blockChainService).validCancelProposal(any(), any());
        doNothing().when(blockChainService).validGovernParam(any(), any(), any(), any());
        try (InputStream coldKeystore =
                     ExecutorTest.class.getClassLoader().getResourceAsStream("staking.keystore")) {
            File targetFile = ResourceUtils.getKeystorePath().resolve("staking.keystore").toFile();
            if (!targetFile.exists() && coldKeystore != null) {
                Files.copy(coldKeystore, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        doNothing().when(blockChainService).validAmount(any(), any(), any(), any());
        doReturn("80000000000000000000").when(blockChainService).getGovernParamValue(any(), any());
    }

    @Test
    void offlinesign() throws Exception {
        OfflineSignOption option = new OfflineSignOption();
        option.setFilelocation(resourceDirectory.resolve("csvfile/transaction_detail.csv"));
        OfflineSignExcutor excutor = new OfflineSignExcutor(null, option);
        excutor.execute(option);
        assertTrue(true);
    }

    @Test
    void sendSignedTx() throws Exception {
        SendSignedTxOption option = new SendSignedTxOption();
        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        String fileLocation =
                Paths.get(ClassLoader.getSystemResource("csvfile/transaction_signature.csv").toURI())
                        .toAbsolutePath()
                        .toString();
        option.setConfig(validatorConfig);
        option.setFilelocation(Paths.get(fileLocation));

        // given
        String transactionStr =
                Files.newBufferedReader(
                        Paths.get(
                                ClassLoader.getSystemResource("platondata/staking_transaction.json").toURI()))
                        .lines()
                        .collect(Collectors.joining())
                        .trim();
        PlatonSendTransaction transaction = PlatonJsonHelp.parseTransaction(transactionStr);

        String receiptStr =
                Files.newBufferedReader(
                        Paths.get(
                                ClassLoader.getSystemResource("platondata/staking_transaction_receipt.json")
                                        .toURI()))
                        .lines()
                        .collect(Collectors.joining())
                        .trim();
        PlatonGetTransactionReceipt receipt = PlatonJsonHelp.parseReceipt(receiptStr);

        PlatonMockHelp.mockWeb3j(web3j, transaction).platonSendRawTransaction(Mockito.anyString());
        PlatonMockHelp.mockWeb3j(web3j, receipt).platonGetTransactionReceipt(Mockito.anyString());
        // when
        SendSignedTxExecutor executor = spy(sendSignedTxExecutor);
        doReturn(emptyContract).when(executor).getEmptyContract(any());
        doReturn(web3j).when(executor).getWeb3j(any());
        TransactionResponse response = new TransactionResponse();
        response.setTransactionReceipt(receipt.getResult());
        when(emptyContract.getTransactionResponse(any())).thenReturn(new RemoteCall<>(() -> response));
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void create_observewallet() {
        String args =
                String.format(
                        "create_observewallet --keystore %s",
                        STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        new MtoolClient().run(args.split(WHITE_SPACE));
        assertTrue(true);
    }

    @Test
    void createWallet() {
        String args = "account new zhangsan";
        new MtoolClient().run(args.split(WHITE_SPACE));
        assertTrue(true);
    }

    @Test
    void createRestrictingPlan() throws Exception {
        RestrictingConfigConverter converter = new RestrictingConfigConverter("--file");
        RestrictingConfig restrictingConfig = converter.convert(RESTRICTING_PLAN_PATH.toAbsolutePath().toString());

        CreateRestrictingPlanOption option = new CreateRestrictingPlanOption();
        option.setRestrictingConfig(restrictingConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter("--keystore");
        Keystore keystore = keystoreConverter.convertKeystore(STAKING_KEYSTORE_PATH.toAbsolutePath().toString(), "123456");
        option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);

        // given
        createRestrictingPlanExecutor = spy(createRestrictingPlanExecutor);

        doReturn(restrictingPlanContract).when(createRestrictingPlanExecutor).getRestrictingPlanContract(any(), any());

        GasProvider gasProvider = new DefaultGasProvider();
        when(restrictingPlanContract.getCreateRestrictingPlanGasProvider(any())).thenReturn(gasProvider);

        when(restrictingPlanContract.createRestrictingPlanReturnTransaction(any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(restrictingPlanContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));

        // when
        createRestrictingPlanExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void createRestrictingPlan_error() throws Exception {
        RestrictingConfigConverter converter = new RestrictingConfigConverter("--file");
        RestrictingConfig restrictingConfig = converter.convert(RESTRICTING_PLAN_PATH_ERROR.toAbsolutePath().toString());

        CreateRestrictingPlanOption option = new CreateRestrictingPlanOption();
        option.setRestrictingConfig(restrictingConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter("--keystore");
        Keystore keystore = keystoreConverter.convertKeystore(STAKING_KEYSTORE_PATH.toAbsolutePath().toString(), "123456");
        option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);

        // given
        createRestrictingPlanExecutor = spy(createRestrictingPlanExecutor);

        doReturn(restrictingPlanContract).when(createRestrictingPlanExecutor).getRestrictingPlanContract(any(), any());

        GasProvider gasProvider = new DefaultGasProvider();
        when(restrictingPlanContract.getCreateRestrictingPlanGasProvider(any())).thenReturn(gasProvider);

        when(restrictingPlanContract.createRestrictingPlanReturnTransaction(any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(restrictingPlanContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));

        // when
        assertThrows(MtoolClientException.class, ()->{
            createRestrictingPlanExecutor.execute(option);
        });

    }


    @Test
    void createRestrictingPlan_observe() throws Exception {
        RestrictingConfigConverter converter = new RestrictingConfigConverter("--file");
        RestrictingConfig restrictingConfig = converter.convert(RESTRICTING_PLAN_PATH.toAbsolutePath().toString());

        CreateRestrictingPlanOption option = new CreateRestrictingPlanOption();
        option.setRestrictingConfig(restrictingConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter("--address");
        Keystore keystore = keystoreConverter.convertObserve(OBSERVE_KEYSTORE_PATH);
        option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);

        // given
        createRestrictingPlanExecutor = spy(createRestrictingPlanExecutor);

        doReturn(restrictingPlanContract).when(createRestrictingPlanExecutor).getRestrictingPlanContract(any(), any());

        GasProvider gasProvider = new DefaultGasProvider();
        when(restrictingPlanContract.getCreateRestrictingPlanGasProvider(any())).thenReturn(gasProvider);

        when(restrictingPlanContract.createRestrictingPlanReturnTransaction(any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(restrictingPlanContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));

        // when
        createRestrictingPlanExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void staking() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--amount");
        StakingAmount stakingAmount = converter.convert("1000000");
        StakingOption option = new StakingOption();
        option.setAmount(stakingAmount);
        KeystoreConverter keystoreConverter = new KeystoreConverter("--keystore");
        Keystore keystore = keystoreConverter.convertKeystore(STAKING_KEYSTORE_PATH.toAbsolutePath().toString(), "123456");
        option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        // given
        stakingExecutor = spy(stakingExecutor);
        doReturn(stakingContract).when(stakingExecutor).getStakingContract(any(), any());

        when(stakingContract.stakingReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(stakingExecutor).checkGasPrice(any());
        stakingExecutor.execute(option);
        assertTrue(true);
    }


    @Test
    void staking_auto() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--autoamount");
        StakingAmount stakingAmount = converter.convert("1000000");
        StakingOption option = new StakingOption();
        option.setAmount(stakingAmount);
        KeystoreConverter keystoreConverter = new KeystoreConverter("--keystore");
        Keystore keystore = keystoreConverter.convertKeystore(STAKING_KEYSTORE_PATH.toAbsolutePath().toString(), "123456");
        option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        // given
        stakingExecutor = spy(stakingExecutor);
        doReturn(stakingContract).when(stakingExecutor).getStakingContract(any(), any());

        when(stakingContract.stakingReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(stakingExecutor).checkGasPrice(any());
        stakingExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void staking_observe() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--amount");
        StakingAmount stakingAmount = converter.convert("1000000");
        StakingOption option = new StakingOption();
        option.setAmount(stakingAmount);
        KeystoreConverter keystoreConverter = new KeystoreConverter("--address");
        Keystore keystore = keystoreConverter.convertObserve(OBSERVE_KEYSTORE_PATH);
        option.setKeystore(keystore);
        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        // given
        stakingExecutor = spy(stakingExecutor);
        doReturn(stakingContract).when(stakingExecutor).getStakingContract(any(), any());

        when(stakingContract.stakingReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(stakingExecutor).checkGasPrice(any());
        stakingExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void staking_auto_observe() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--autoamount");
        StakingAmount stakingAmount = converter.convert("1000000");
        StakingOption option = new StakingOption();
        option.setAmount(stakingAmount);
        KeystoreConverter keystoreConverter = new KeystoreConverter("--address");
        Keystore keystore = keystoreConverter.convertObserve(OBSERVE_KEYSTORE_PATH);
        option.setKeystore(keystore);
        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        // given
        stakingExecutor = spy(stakingExecutor);
        doReturn(stakingContract).when(stakingExecutor).getStakingContract(any(), any());

        when(stakingContract.stakingReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(stakingExecutor).checkGasPrice(any());
        stakingExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void unstaking() throws Exception {
        UnstakingOption option = new UnstakingOption();
        Keystore keystore = new Keystore();
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        keystore.setCredentials(credentials);
        String address = credentials.getAddress();
        option.setKeystore(keystore);
        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);

        UnstakingExecutor executor = Mockito.spy(unstakingExecutor);
        doReturn(stakingContract).when(executor).getStakingContract(any(), any());

        when(stakingContract.unStakingReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void declareVersion() throws Exception {
        DeclareVersionOption option = new DeclareVersionOption();
        Keystore keystore = new Keystore();
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        keystore.setCredentials(credentials);
        String address = credentials.getAddress();
         option.setKeystore(keystore);

        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        DeclareVersionExecutor executor = Mockito.spy(declareVersionExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.declareVersionReturnTransaction(any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void increaseStaking() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--amount");
        StakingAmount stakingAmount = converter.convert("10");

        IncreaseStakingOption option = new IncreaseStakingOption();
        option.setAmount(stakingAmount);
        ValidatorConfig validatorConfig =
                JSON.parseObject(
                        Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("validator_config.json")),
                        ValidatorConfig.class);
        option.setConfig(validatorConfig);
        Keystore keystore = new Keystore();
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        keystore.setCredentials(credentials);
        String address = credentials.getAddress();

        option.setKeystore(keystore);
        increaseStakingExecutor = Mockito.spy(increaseStakingExecutor);
        doReturn(stakingContract)
                .when(increaseStakingExecutor)
                .getStakingContract(any(), any());
        GasProvider gp = mock(GasProvider.class);
        doReturn(gp).when(increaseStakingExecutor).checkGasPrice(any());
        when(stakingContract.addStakingReturnTransaction(any(), any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(increaseStakingExecutor).checkGasPrice(any());
        increaseStakingExecutor.execute(option);
        assertTrue(true);
    }

    @Test
    void submitTextProposal() throws Exception {
        SubmitTextProposalOption option = new SubmitTextProposalOption();
        option.setPidId("123");

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        SubmitTextProposalExecutor executor = Mockito.spy(submitTextProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.submitProposalReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void submitVersionProposal() throws Exception {
        SubmitVersionProposalOption option = new SubmitVersionProposalOption();
        option.setEndVotingRounds(BigInteger.valueOf(10));
        option.setNewversion(BigInteger.valueOf(10));
        option.setPidId("121");

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        SubmitVersionProposalExecutor executor = Mockito.spy(submitVersionProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.submitProposalReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void submitCancelProposal() throws Exception {
        SubmitCancelProposalOption option = new SubmitCancelProposalOption();
        option.setProposalid("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");
        option.setPidId("121");
        option.setEndVotingRounds(BigInteger.valueOf(10));

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        SubmitCancelProposalExecutor executor = Mockito.spy(submitCancelProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.submitProposalReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void submitParamProposal() throws Exception {
        SubmitParamProposalOption option = new SubmitParamProposalOption();
        option.setPidId("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");
        option.setModule("module");
        option.setParamName("name");
        option.setParamValue("value");

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        SubmitParamProposalExecutor executor = Mockito.spy(submitParamProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.submitProposalReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void updateValidator() throws Exception {
        UpdateValidatorOption option = new UpdateValidatorOption();
        option.setConfig(mockConfig());

        Keystore keystore = new Keystore();
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        keystore.setCredentials(credentials);
        String address = credentials.getAddress();
        option.setKeystore(keystore);


        // given
        UpdateValidatorExecutor executor = Mockito.spy(updateValidatorExecutor);
        doReturn(stakingContract).when(executor).getStakingContract(any(), any());

        when(stakingContract.updateStakingInfoReturnTransaction(any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(stakingContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));

        ValidatorConfig validatorConfig = mockConfig();
        Node node = new Node();
        BeanUtils.copyProperties(node, validatorConfig);
        doReturn(node).when(executor).getStakingInfo(any(), any());

        option.setNodeName("testNewNodeName");
        option.setExternalId("");
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void voteTextProposal() throws Exception {
        VoteTextProposalOption option = new VoteTextProposalOption();
        option.setProposalid("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");
        option.setOpinion(VoteOption.NAYS);

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        VoteTextProposalExecutor executor = Mockito.spy(voteTextProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.voteReturnTransaction(any(), any(), any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void voteVersionProposal() throws Exception {
        VoteVersionProposalOption option = new VoteVersionProposalOption();
        option.setProposalid("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        VoteVersionProposalExecutor executor = Mockito.spy(voteVersionProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.voteReturnTransaction(any(), any(), any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void voteCancelProposal() throws Exception {
        VoteCancelProposalOption option = new VoteCancelProposalOption();
        option.setProposalid("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");
        option.setOpinion(VoteOption.NAYS);

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        VoteCancelProposalExecutor executor = Mockito.spy(voteCancelProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.voteReturnTransaction(any(), any(), any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void voteParamProposal() throws Exception {
        VoteParamProposalOption option = new VoteParamProposalOption();
        option.setProposalid("0x3235fbade28c02a7942868b0c3780632b6e68f0969e2c6fd108ea4ddf0756808");
        option.setOpinion(VoteOption.NAYS);

        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());
        option.setConfig(validatorConfig);

        KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
        Keystore keystore =
                keystoreConverter.convert(STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        option.setKeystore(keystore);

        VoteParamProposalExecutor executor = Mockito.spy(voteParamProposalExecutor);
        doReturn(proposalContract).when(executor).getProposalContract(any(), any());

        when(proposalContract.voteReturnTransaction(any(), any(), any(), any(), any()))
                .thenReturn(new RemoteCall<>(() -> transaction));
        when(proposalContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        doReturn(mock(GasProvider.class)).when(executor).checkGasPrice(any());
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void txTransfer() throws Exception {
        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());

        TxOptions.TransferOption option = new TxOptions.TransferOption();
        option.setConfig(validatorConfig);
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        String address = credentials.getAddress();
        Keystore keystore = new Keystore();
        keystore.setCredentials(credentials);
        keystore.setType(Keystore.Type.NORMAL);
        option.setKeystore(keystore);
        option.setLat(BigDecimal.ONE);
        option.setTo("lat1k92gm4sszzn59ntxlwrryj4nu2f4tpjttq6vtp");
        option.setGasProvider(new DefaultGasProvider());
        TxTransferExecutor executor = Mockito.spy(txTransferExecutor);

        doReturn(transfer).when(executor).getTransfer(any(), any(Credentials.class));
        TransactionReceipt receipt = new TransactionReceipt();
        receipt.setTransactionHash("0xsuccess");
        when(transfer.sendFunds(anyString(), any(), any(), any(), any())).thenReturn(new RemoteCall<>(() -> receipt));
        executor.execute(option);
        assertTrue(true);

        // 离线钱包
        option.setKeystore(observedKeystore);
        doReturn(transfer).when(executor).getTransfer(any(), anyString());
        receipt.setTransactionHash(mockOfflineTransaction().getTransactionHash());
        when(transfer.sendFunds(anyString(), any(), any(), any(), any())).thenReturn(new RemoteCall<>(() -> receipt));
        executor.execute(option);
        assertTrue(true);
    }

    @Test
    void txDelegate() throws Exception {
        StakingAmountConverter converter = new StakingAmountConverter("--amount");
        ValidatorConfigConverter configConverter = new ValidatorConfigConverter(AllParams.CONFIG);
        ValidatorConfig validatorConfig =
                configConverter.convert(VALIDATOR_CONFIG_PATH.toAbsolutePath().toString());


        StakingAmount delegateAmount = converter.convert("1000000");

        TxOptions.DelegateOption option = new TxOptions.DelegateOption();
        option.setAmount(delegateAmount);
        option.setConfig(validatorConfig);
        Credentials credentials =
                WalletUtils.loadCredentials("123456", STAKING_KEYSTORE_PATH.toAbsolutePath().toString());
        Keystore keystore = new Keystore();
        String address = credentials.getAddress();
        keystore.setCredentials(credentials);
        keystore.setType(Keystore.Type.NORMAL);
        option.setKeystore(keystore);
        option.setNodeId("0xasdf");
        TxDelegateExecutor executor = Mockito.spy(txDelegateExecutor);

        doReturn(delegateContract).when(executor).getDelegateContract(any(), any(Credentials.class));
        when(delegateContract.delegateReturnTransaction(anyString(), any(), any(), any())).thenReturn(new RemoteCall<>(() -> transaction));
        GasProvider gasProvider = new DefaultGasProvider();
        when(delegateContract.getDelegateGasProvider(anyString(), any(), any())).thenReturn(gasProvider);
        when(delegateContract.getDelegateGasProvider(anyString(), any(), any())).thenReturn(gasProvider);
        when(delegateContract.getTransactionResponse(any()))
                .thenReturn(new RemoteCall<>(() -> successResponse));
        executor.execute(option);
        assertTrue(true);

        // 离线钱包
        option.setKeystore(observedKeystore);
        doReturn(delegateContract).when(executor).getDelegateContract(any(), anyString());
        when(delegateContract.delegateReturnTransaction(anyString(), any(), any(), any())).thenReturn(new RemoteCall<>(() -> mockOfflineTransaction()));
        executor.execute(option);
        assertTrue(true);
    }

    private PlatonSendTransaction mockOfflineTransaction() throws IOException {
        PlatonSendTransaction transaction = new PlatonSendTransaction();
        TransactionEntity entity = JSON.parseObject(
                Files.newInputStream(resourceDirectory.resolve("transaction_detail.json")),
                TransactionEntity.class);
        entity.setChainId(CLIENT_CONFIG.getChainId());
        transaction.setResult(JSON.toJSONString(entity));
        return transaction;
    }
}
