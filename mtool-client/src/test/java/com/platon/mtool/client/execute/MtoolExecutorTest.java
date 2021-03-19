package com.platon.mtool.client.execute;

import com.platon.contracts.ppos.dto.BaseResponse;
import com.platon.mtool.client.test.MtoolParameterResolver;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.exception.MtoolException;
import com.platon.mtool.common.exception.MtoolPlatonException;
import com.platon.mtool.common.exception.MtoolPlatonExceptionCode;
import com.platon.mtool.common.utils.PlatOnUnit;
import com.platon.protocol.core.methods.response.PlatonSendTransaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
@ExtendWith(MtoolParameterResolver.class)
class MtoolExecutorTest {

  private MtoolExecutor mtoolExecutor = new StakingExecutor(null, null);

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void handleException() {
    MtoolException exception =
        assertThrows(
            MtoolException.class,
            () -> mtoolExecutor.handleException(new MtoolClientException("exception test")));
    assertEquals("exception test", exception.getMessage());
  }

  @Test
  void platonException(ValidatorConfig validatorConfig) {
    String nodeId = validatorConfig.getNodePublicKey();
    BigInteger balance = PlatOnUnit.latToVon(new BigInteger("5000000"));
    MtoolPlatonException e301111 = mtoolExecutor.platonException(301111, nodeId, balance);
    assertEquals(301111, e301111.getCode());
    assertEquals(
        "Insufficient wallet balance(Total payment: 5000000.000000000000000000 LAT)",
        e301111.getMessage());

    MtoolPlatonException e301115 = mtoolExecutor.platonException(301115, nodeId, balance);
    assertEquals(301115, e301115.getCode());
    assertEquals(
        String.format(MtoolPlatonExceptionCode.E301115.getMessage(), nodeId), e301115.getMessage());

    MtoolPlatonException e1 = mtoolExecutor.platonException(1, nodeId, balance);
    assertEquals(1, e1.getCode());
    assertEquals(MtoolPlatonExceptionCode.SYSTEM_ERROR.getMessage(), e1.getMessage());
  }

  @Test
  void echoResult() {
    BaseResponse response = new BaseResponse();
    response.setCode(0);
    PlatonSendTransaction transaction = new PlatonSendTransaction();
    transaction.setResult("transaction");
    mtoolExecutor.echoResult(transaction, response, null, null);

    response.setCode(301111);
    assertThrows(
        MtoolPlatonException.class,
        () -> mtoolExecutor.echoResult(transaction, response, null, BigInteger.ZERO));
  }
}
