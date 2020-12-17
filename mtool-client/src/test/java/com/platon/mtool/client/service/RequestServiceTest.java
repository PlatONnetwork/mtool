//package com.platon.mtool.client.service;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.platon.mtool.client.httpclients.HttpJunitServer;
//import com.platon.mtool.client.test.MtoolParameterResolver;
//import com.platon.mtool.common.entity.ValidatorConfig;
//import com.platon.mtool.common.entity.http.PeriodInfo;
//import java.io.File;
//import java.math.BigInteger;
//import java.util.List;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
///** Created by liyf. */
//@ExtendWith(MtoolParameterResolver.class)
//class RequestServiceTest {
//
//  private static int serverPort = 7788;
//
//  private RequestService requestService = RequestService.singleton();
//
//  @BeforeAll
//  static void setUp() throws Exception {
//    HttpJunitServer.start(serverPort);
//  }
//
//  @AfterAll
//  static void tearDown() {
//    HttpJunitServer.shutdown();
//  }
//
//  @Test
//  void postDivideResult(ValidatorConfig validatorConfig) {
//    File rewardConfigFile =
//        new File(
//            ClassLoader.getSystemResource("csvfile/delegate_reward_result_expect.csv").getPath());
//    requestService.postDivideResult(validatorConfig.getNodePublicKey(), rewardConfigFile);
//    assertTrue(true);
//  }
//
//  @Test
//  void postPrepareWeb3j(ValidatorConfig validatorConfig) {
//    requestService.postPrepareWeb3j(validatorConfig);
//    assertTrue(true);
//  }
//
//  @Test
//  void postDividePeriod(ValidatorConfig validatorConfig) {
//    List<PeriodInfo> pair =
//        requestService.postDividePeriod(
//            BigInteger.valueOf(1), BigInteger.valueOf(480), validatorConfig);
//    //        assertEquals(3, pair.size());
//    assertTrue(true);
//  }
//
//  @Test
//  void postGenFile(ValidatorConfig validatorConfig) {
//    File rewardConfigFile = new File(ClassLoader.getSystemResource("reward_config.json").getPath());
//    byte[] bytes = requestService.postGenFile(1, validatorConfig, rewardConfigFile);
//    System.out.println(new String(bytes));
//    bytes = requestService.postGenFile(2, validatorConfig, rewardConfigFile);
//    System.out.println(new String(bytes));
//    assertTrue(true);
//  }
//
//  @Test
//  void postGenTotalFile(ValidatorConfig validatorConfig) {
//    File rewardConfigFile = new File(ClassLoader.getSystemResource("reward_config.json").getPath());
//    byte[] bytes =
//        requestService.postGenTotalFile(
//            BigInteger.valueOf(1),
//            BigInteger.valueOf(320),
//            null,
//            validatorConfig,
//            rewardConfigFile);
//    System.out.println(new String(bytes));
//    assertTrue(true);
//  }
//}
