package com.platon.mtool.common.web3j;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.protocol.Web3j;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/** Created by liyf */
class Web3jUtilTest {
  private static Path resourceDirectory = Paths.get("src", "test", "resources");

  private ValidatorConfig mockConfig() throws IOException {
    ValidatorConfig validatorConfig =
        JSON.parseObject(
            Files.newInputStream(resourceDirectory.resolve("validator_config.json")),
            ValidatorConfig.class);
    validatorConfig.setNodeAddress("https://username:password@test187.com");
    validatorConfig.setCertificate(
        resourceDirectory.resolve("ssl/server.crt").toAbsolutePath().toString());
    return validatorConfig;
  }

  @Test
  void shoutInit() throws IOException {
    Web3j web3j = Web3jUtil.getFromConfig(mockConfig());
    assertNotNull(web3j);
  }
}
