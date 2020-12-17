package com.platon.mtool.client.converter;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.test.MtoolParameterResolver;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.ValidatorConfig;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Created by liyf. */
@ExtendWith(MtoolParameterResolver.class)
class ValidatorConfigConverterTest {

  @TempDir protected Path tempDir;

  private ValidatorConfigConverter converter = new ValidatorConfigConverter(AllParams.CONFIG);

  @Test
  void convert(ValidatorConfig config) throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("validator_config.json").toURI());
    String filepath = originPath.toAbsolutePath().toString();
    ValidatorConfig validatorConfig = converter.convert(filepath);
    assertTrue(EqualsBuilder.reflectionEquals(config, validatorConfig));
  }

  @Test
  void convertNotFound() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> converter.convert("/path/nofound"));
    assertTrue(exception.getMessage().contains("No such file"));
  }

  @Test
  void convertInvalid() throws IOException {
    Path path = tempDir.resolve("errorJson");
    Files.write(path, "{".getBytes());
    ParameterException exception =
        assertThrows(
            ParameterException.class, () -> converter.convert(path.toAbsolutePath().toString()));
    assertTrue(exception.getMessage().contains("Invalid validator config file"));
  }

  @Test
  void convertConfigInvalid(ValidatorConfig config)
      throws IOException, InvocationTargetException, IllegalAccessException {
    ValidatorConfig validatorConfig = new ValidatorConfig();
    BeanUtils.copyProperties(validatorConfig, config);
    validatorConfig.setNodePublicKey("pubkey");
    Path path = tempDir.resolve("invalid");
    Files.write(path, JSON.toJSONBytes(validatorConfig));
    ParameterException exception =
        assertThrows(
            ParameterException.class, () -> converter.convert(path.toAbsolutePath().toString()));
    assertTrue(exception.getMessage().contains("Invalid nodePublicKey"));
  }
}
