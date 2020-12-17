package com.platon.mtool.client.service;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.validate.UpdateValidatorChecks;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/** Created by liyf. */
class ParamValidatorTest {
  private BlockChainService blockChainService = BlockChainService.singleton();

  private static Path resourceDirectory = Paths.get("src", "test", "resources");

  private ValidatorConfig mockConfig() throws IOException {
    return JSON.parseObject(
        Files.newInputStream(resourceDirectory.resolve("validator_config.json")),
        ValidatorConfig.class);
  }

  @Test
  void invalidNodePublicKey() throws IOException {
    ValidatorConfig config = mockConfig();
    config.setNodePublicKey("invalidNodeId");

    MtoolClientException exception =
        assertThrows(MtoolClientException.class, () -> blockChainService.validConfig(config));
    assertEquals("validator param is invalid", exception.getMessage());
  }


  @Test
  void valid() throws IOException {
    // language=JSON
    ValidatorConfig config = mockConfig();
    blockChainService.validConfig(config);
    assertTrue(true);
  }


  @Test
  void inValidPart() throws IOException {
    // language=JSON
    ValidatorConfig config = mockConfig();
    config.setNodePort(-1);

    MtoolClientException exception =
        assertThrows(
            MtoolClientException.class,
            () -> blockChainService.validConfig(config, UpdateValidatorChecks.class));
    assertEquals("validator param is invalid", exception.getMessage());
  }

  // 缺少一个参数
  @Test
  void validIgnoreNullPart() {
    ValidatorConfig config = new ValidatorConfig();
    blockChainService.validConfig(config, UpdateValidatorChecks.class);
    assertTrue(true);
  }
}
