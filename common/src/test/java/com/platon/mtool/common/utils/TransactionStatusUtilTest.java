package com.platon.mtool.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.platon.mtool.common.enums.TransactionStatus;
import com.platon.protocol.core.methods.response.PlatonGetTransactionReceipt;
import com.platon.protocol.core.methods.response.TransactionReceipt;
import com.platon.utils.Numeric;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Created by liyf. */
class TransactionStatusUtilTest {

  private static TransactionReceipt parseReceipt(String jsonStr) {
    JSONObject jsonObject = JSON.parseObject(jsonStr);
    TransactionReceipt receipt = JSON.parseObject(jsonStr, TransactionReceipt.class);
    receipt.setBlockNumber(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("blockNumber").toString())));
    receipt.setGasUsed(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("gasUsed").toString())));
    return receipt;
  }

  @Test
  void getTransactionStatus() throws URISyntaxException, IOException {
    String receiptStr =
        Files.newBufferedReader(
                Paths.get(ClassLoader.getSystemResource("platondata/staking_receipt.json").toURI()))
            .lines()
            .collect(Collectors.joining())
            .trim();
    PlatonGetTransactionReceipt platonGetTransactionReceipt = new PlatonGetTransactionReceipt();
    TransactionReceipt receipt = parseReceipt(receiptStr);
    platonGetTransactionReceipt.setResult(receipt);
    TransactionStatus status =
        TransactionStatusUtil.getTransactionStatus(
            "0x1000000000000000000000000000000000000002", receipt);
    System.out.println(status);
    assertEquals(TransactionStatus.SUCCESS, status);
  }
}
