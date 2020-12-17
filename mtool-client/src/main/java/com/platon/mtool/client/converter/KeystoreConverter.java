package com.platon.mtool.client.converter;

import com.alaya.crypto.Address;
import com.alaya.crypto.CipherException;
import com.alaya.crypto.Credentials;
import com.alaya.crypto.WalletUtils;
import com.alaya.parameters.NetworkParameters;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.web3j.Keystore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 钱包转换器
 *
 * <p>Created by liyf.
 */
public class KeystoreConverter extends BaseConverter<Keystore> {

  private static final Logger logger = LoggerFactory.getLogger(KeystoreConverter.class);
  private PathConverter pathConverter = new PathConverter(getOptionName());

  public KeystoreConverter(String optionName) {
    super(optionName);
  }

  @Override
  public Keystore convert(String value) {
    Path path = pathConverter.convert(value);
    String pathStr = path.toAbsolutePath().toString();
    Keystore keystore = new Keystore();
    if (getOptionName().equals(AllParams.ADDRESS)) {
      return convertObserve(path);
    } else if (getOptionName().equals(AllParams.KEYSTORE)) {
      char[] pass = PrintUtils.readPassword("please input keystore password: ");
      return convertKeystore(pathStr, new String(pass));
    }
    return keystore;
  }

  public Keystore convertObserve(Path path ) {
    String pathStr = path.toAbsolutePath().toString();
    Keystore keystore = new Keystore();
    try (InputStream is = Files.newInputStream(path)) {
      keystore = JSON.parseObject(is, Keystore.class);
    } catch (IOException | JSONException e) {
      throw new ParameterException(
              getErrorString(pathStr, "Invalid wallet observe keystore file"));
    }
    keystore.setFilepath(pathStr);
    if (!Keystore.Type.OBSERVE.equals(keystore.getType())) {
      throw new ParameterException("address is not an observe keystore");
    }
    return keystore;
  }

  public Keystore convertKeystore(String pathStr, String password){
    Keystore keystore = new Keystore();
    Credentials credentials;
    try {
      credentials = WalletUtils.loadCredentials(password, pathStr);
    } catch (IOException e) {
      logger.error("Invalid wallet keystore file", e);
      throw new ParameterException(getErrorString(pathStr, "Invalid wallet keystore file"));
    } catch (CipherException e) {
      logger.error("Incorrect password", e);
      throw new ParameterException(getErrorString(pathStr, "Incorrect password"));
    }
    keystore.setType(Keystore.Type.NORMAL);
    keystore.setCredentials(credentials);
    keystore.setFilepath(pathStr);
    Address address = new Address(credentials.getAddress(NetworkParameters.MainNetParams),credentials.getAddress(NetworkParameters.TestNetParams));
    keystore.setAddress(address);
    return keystore;
  }

  @Override
  protected String getErrorString(String value, String to) {
    return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
  }
}
