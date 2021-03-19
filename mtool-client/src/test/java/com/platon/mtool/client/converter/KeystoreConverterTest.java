package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.test.MtoolParameterResolver;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.web3j.Keystore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/** Created by liyf. */
@ExtendWith(MtoolParameterResolver.class)
class KeystoreConverterTest {

  @TempDir protected Path tempDir;
  private KeystoreConverter keystoreConverter = new KeystoreConverter(AllParams.KEYSTORE);
  private KeystoreConverter addressConverter = new KeystoreConverter(AllParams.ADDRESS);

  @BeforeAll
  static void Setup(){
    CliConfigUtils.loadProperties();
  }

  @Test
  void convertKeystore() throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("staking.keystore").toURI());
    String filepath = originPath.toAbsolutePath().toString();

    Keystore keystore = keystoreConverter.convert(filepath);
    assertNotNull(keystore.getAddress());
    assertEquals(Keystore.Type.NORMAL, keystore.getType());
    assertEquals(
        "lat1cy2uat0eukfrxv897s5s8lnljfka5ewjtnrfhx", keystore.getCredentials().getAddress());
  }

  @Test
  void convertAddress() throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("staking_observed.json").toURI());
    String filepath = originPath.toAbsolutePath().toString();

    Keystore keystore = addressConverter.convert(filepath);
    assertEquals("lat1cy2uat0eukfrxv897s5s8lnljfka5ewjtnrfhx", keystore.getAddress());
    assertEquals(Keystore.Type.OBSERVE, keystore.getType());
    assertNull(keystore.getCredentials());
  }

  @Test
  void convertKeystoreJsonError() throws IOException {
    Path path = tempDir.resolve("errorJson");
    Files.write(path, "{".getBytes());
    ParameterException exception =
        assertThrows(
            ParameterException.class,
            () -> keystoreConverter.convert(path.toAbsolutePath().toString()));
    assertTrue(exception.getMessage().contains("Invalid wallet keystore file"));
  }

  @Test
  void convertKeystorePassError() throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("staking_observed.json").toURI());
    ParameterException exception =
        assertThrows(
            ParameterException.class,
            () -> keystoreConverter.convert(originPath.toAbsolutePath().toString()));
    assertTrue(exception.getMessage().contains("Incorrect password"));
  }

  @Test
  void convertAddressJsonError() throws IOException {
    Path path = tempDir.resolve("errorJson");
    Files.write(path, "{".getBytes());
    ParameterException exception =
        assertThrows(
            ParameterException.class,
            () -> addressConverter.convert(path.toAbsolutePath().toString()));
    System.out.println(exception.getMessage());
    assertTrue(exception.getMessage().contains("Invalid wallet observe keystore file"));
  }

  @Test
  void convertAddressTypeError() throws URISyntaxException {
    Path originPath = Paths.get(ClassLoader.getSystemResource("staking.keystore").toURI());
    ParameterException exception =
        assertThrows(
            ParameterException.class,
            () -> addressConverter.convert(originPath.toAbsolutePath().toString()));
    System.out.println(exception.getMessage());
    assertTrue(exception.getMessage().contains("Invalid wallet observe keystore file"));
  }

  @Test
  void keystore_error() throws Exception {
    Path originPath = Paths.get(ClassLoader.getSystemResource("staking_error.keystore").toURI());
    String filepath = originPath.toAbsolutePath().toString();

    KeystoreConverter keystoreConverter = new KeystoreConverter("--keystore");

    Keystore keystore;
    assertThrows(com.beust.jcommander.ParameterException.class, ()->{
      keystoreConverter.convertKeystore(filepath, "123456");
    });
  }
}
