//package com.platon.mtool.client.execute;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.anyString;
//import static org.mockito.Mockito.doReturn;
//import static org.mockito.Mockito.when;
//
//import com.platon.mtool.client.MtoolClient;
//import com.platon.mtool.client.converter.KeystoreConverter;
//import com.platon.mtool.client.httpclients.HttpJunitServer;
//import com.platon.mtool.client.options.SendSignedTxOption;
//import com.platon.mtool.client.test.MtoolParameterResolver;
//import com.platon.mtool.client.test.PlatonJsonHelp;
//import com.platon.mtool.client.test.PlatonMockHelp;
//import com.platon.mtool.client.tools.ResourceUtils;
//import com.platon.mtool.common.AllParams;
//import com.platon.mtool.common.entity.ValidatorConfig;
//import com.platon.mtool.common.utils.PlatOnUnit;
//import com.platon.mtool.common.web3j.EmptyContract;
//import com.platon.mtool.common.web3j.Keystore;
//import com.alaya.contracts.ppos.dto.TransactionResponse;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigInteger;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.mockito.junit.jupiter.MockitoSettings;
//import org.mockito.quality.Strictness;
//import com.alaya.protocol.admin.Admin;
//import com.alaya.protocol.core.RemoteCall;
//import com.alaya.protocol.core.methods.response.PlatonGasPrice;
//import com.alaya.protocol.core.methods.response.PlatonGetBalance;
//import com.alaya.protocol.core.methods.response.PlatonGetTransactionReceipt;
//import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
//import com.alaya.protocol.core.methods.response.TransactionReceipt;
//import com.alaya.tx.Transfer;
//
///** Created by liyf. */
//@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
//@ExtendWith(MtoolParameterResolver.class)
//class ServerExecutorTest {
//
//  private static final String REWARD_FILE =
//      ClassLoader.getSystemResource("csvfile/delegate_reward_summary_expect.csv").getPath();
//  private static final String OBSERVE_KEYSTORE =
//      ClassLoader.getSystemResource("staking_observed.json").getPath();
//  private static final int SERVER_PORT = 7788;
//  private static final String WHITE_SPACE = "\\s+";
//  private static Path resourceDirectory = Paths.get("src", "test", "resources");
//  private static final Path VALIDATOR_CONFIG_PATH =
//      resourceDirectory.resolve("validator_config.json");
//  private static final Path REWARD_CONFIG_PATH = resourceDirectory.resolve("reward_config.json");
//  private static final Path REWARD_FILE_PATH =
//      resourceDirectory.resolve("csvfile/delegate_reward_summary_expect.csv");
//  private static final Path OBSERVE_KEYSTORE_PATH =
//      resourceDirectory.resolve("staking_observed.json");
//  private static final Path STAKING_KEYSTORE_PATH = resourceDirectory.resolve("staking.keystore");
//  private static String STAKING_KEYSTORE =
//      ClassLoader.getSystemResource("staking.keystore").getPath();
//  private static Admin web3j;
//  private static Transfer transfer;
//  private static Keystore observedKeystore;
//  private static Keystore stakingKeystore;
//  @Mock private EmptyContract emptyContract;
//
//  @BeforeAll
//  static void before(@Mock Admin web3j, @Mock Transfer transfer) throws Exception {
//    KeystoreConverter addressConverter = new KeystoreConverter(AllParams.ADDRESS);
//    observedKeystore =
//        addressConverter.convert(
//            resourceDirectory.resolve("staking_observed.json").toAbsolutePath().toString());
//
//    KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
//    stakingKeystore =
//        keystoreConverter.convert(
//            resourceDirectory.resolve("staking.keystore").toAbsolutePath().toString());
//
//    ServerExecutorTest.web3j = web3j;
//    ServerExecutorTest.transfer = transfer;
//    HttpJunitServer.start(SERVER_PORT);
//    try (InputStream is =
//        ServerExecutorTest.class.getClassLoader().getResourceAsStream("config.properties")) {
//      File targetFile = ResourceUtils.getRootPath().resolve("config.properties").toFile();
//      if (!targetFile.exists() && is != null) {
//        Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    // given
//    // gasPrice 1000000000
//    // gasLimit 21000
//    PlatonGasPrice platonGasPrice = new PlatonGasPrice();
//    platonGasPrice.setResult("0x" + new BigInteger("1000000000").toString(16));
//    PlatonMockHelp.mockWeb3j(ServerExecutorTest.web3j, platonGasPrice)
//        .platonGasPrice(); // 1000000000
//    PlatonGetBalance platonGetBalance = new PlatonGetBalance();
//    platonGetBalance.setResult(
//        "0x" + PlatOnUnit.latToVon(new BigInteger("20000000000")).toString(16));
//    PlatonMockHelp.mockWeb3j(ServerExecutorTest.web3j, platonGetBalance)
//        .platonGetBalance(anyString(), any());
//
//    String receiptStr =
//        Files.newBufferedReader(
//                Paths.get(
//                    ClassLoader.getSystemResource("platondata/staking_transaction_receipt.json")
//                        .toURI()))
//            .lines()
//            .collect(Collectors.joining())
//            .trim();
//    PlatonGetTransactionReceipt receipt = PlatonJsonHelp.parseReceipt(receiptStr);
//    RemoteCall<TransactionReceipt> remoteCall = new RemoteCall<>(receipt::getResult);
//    when(ServerExecutorTest.transfer.sendFunds(anyString(), any(), any(), any(), any()))
//        .thenReturn(remoteCall);
//
//    String transactionStr =
//        Files.newBufferedReader(
//                Paths.get(
//                    ClassLoader.getSystemResource("platondata/staking_transaction.json").toURI()))
//            .lines()
//            .collect(Collectors.joining())
//            .trim();
//    PlatonSendTransaction transaction = PlatonJsonHelp.parseTransaction(transactionStr);
//
//    PlatonMockHelp.mockWeb3j(ServerExecutorTest.web3j, transaction)
//        .platonSendRawTransaction(Mockito.anyString());
//    PlatonMockHelp.mockWeb3j(ServerExecutorTest.web3j, receipt)
//        .platonGetTransactionReceipt(Mockito.anyString());
//  }
//
//  @AfterAll
//  static void after() {
//    HttpJunitServer.shutdown();
//  }
//
//  static Stream<String> cmdGenerator() {
//    return Stream.of(
//        String.format(
//            "gen_reward --start_block 1201 --end_block 1500 --rewardconfig %s --config %s",
//            REWARD_CONFIG_PATH.toAbsolutePath().toString(),
//            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
//        String.format(
//            "reward_divide --reward_file %s --keystore %s --config %s",
//            REWARD_FILE_PATH.toAbsolutePath().toString(),
//            STAKING_KEYSTORE_PATH.toAbsolutePath().toString(),
//            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()),
//        String.format(
//            "reward_divide --reward_file %s --address %s --config %s",
//            REWARD_FILE_PATH.toAbsolutePath().toString(),
//            OBSERVE_KEYSTORE_PATH.toAbsolutePath().toString(),
//            VALIDATOR_CONFIG_PATH.toAbsolutePath().toString()));
//  }
//
//  @ParameterizedTest
//  @MethodSource("cmdGenerator")
//  void allCommand(String args) {
//    try {
//      new MtoolClient().run(args.split(WHITE_SPACE));
//    } catch (Exception e) {
//      Assertions.fail(e);
//    }
//    assertTrue(true);
//  }
//}
