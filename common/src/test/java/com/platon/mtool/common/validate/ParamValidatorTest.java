package com.platon.mtool.common.validate;

import com.alibaba.fastjson.JSON;
import com.platon.mtool.common.entity.ValidatorConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Created by liyf. */
class ParamValidatorTest {

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

    assertEquals(1, ParamValidator.validConfig(config).size());
  }


  @Test
  void valid() throws IOException {
    ValidatorConfig config = mockConfig();

    assertEquals(0, ParamValidator.validConfig(config).size());
  }

  @Test
  void validPart() throws IOException {
    ValidatorConfig config = mockConfig();
    config.setNodePublicKey("invalidNodeId");

    assertEquals(0, ParamValidator.validConfig(config, UpdateValidatorChecks.class).size());
  }


  // 缺少一个参数
  @Test
  void validIgnoreNullPart() {
    ValidatorConfig config = new ValidatorConfig();

    assertEquals(0, ParamValidator.validConfig(config, UpdateValidatorChecks.class).size());
  }
}
