package com.platon.mtool.client.tools;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class CliConfigUtilsTest {

  @BeforeAll
  static void before() {
    try (InputStream is =
        CliConfigUtilsTest.class.getClassLoader().getResourceAsStream("config.properties")) {
      File targetFile = ResourceUtils.getRootPath().resolve("config.properties").toFile();
      if (!targetFile.exists() && is != null) {
        Files.copy(is, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //@Test
  /*void load() {
    assertEquals("127.0.0.1", CLIENT_CONFIG.getHost());
    assertEquals(Integer.valueOf(7788), CLIENT_CONFIG.getPort());
    assertEquals("http://127.0.0.1:7788", CLIENT_CONFIG.getAddress());
  }*/

}
