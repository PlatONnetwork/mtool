package com.platon.mtool.common.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.web3j.TransactionEntity;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class HashUtilTest {

  private static Path resourceDirectory = Paths.get("src", "test", "resources");

  @Test
  void hashTransaction() throws IOException {
    String hash = HashUtil.hashTransaction(mockData());
    System.out.println(hash);
    assertTrue(true);
  }

  @Test
  void isTransactionValid() throws IOException {
    assertTrue(HashUtil.isTransactionValid(mockData()));
  }

  private TransactionEntity mockData() throws IOException {
    return JSON.parseObject(
        Files.newInputStream(resourceDirectory.resolve("transaction_detail.json")),
        TransactionEntity.class);
  }
}
