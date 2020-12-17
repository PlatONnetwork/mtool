package com.platon.mtool.client.converter;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.validate.ParamValidator;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 验证人配置文件转换器
 *
 * <p>Created by liyf.
 */
public class ValidatorConfigConverter extends BaseConverter<ValidatorConfig> {

  private static final String LINE_SEPARATOR = System.getProperty("line.separator");
  PathConverter pathConverter = new PathConverter(getOptionName());

  public ValidatorConfigConverter(String optionName) {
    super(optionName);
  }

  @Override
  public ValidatorConfig convert(String value) {
    Path path = pathConverter.convert(value);
    String pathStr = path.toAbsolutePath().toString();

    ValidatorConfig validatorConfig;
    try (InputStream is = Files.newInputStream(path)) {
      validatorConfig = JSON.parseObject(is, ValidatorConfig.class);
    } catch (NoSuchFileException e) {
      throw new ParameterException(getErrorString(pathStr, "No such validator config file"));
    } catch (Exception e) {
      throw new ParameterException(getErrorString(pathStr, "Invalid validator config file"));
    }

    validConfig(pathStr, validatorConfig);
    return validatorConfig;
  }

  @Override
  protected String getErrorString(String value, String to) {
    return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
  }

  protected void validConfig(String configPath, ValidatorConfig validatorConfig) {
    Map<String, String> invalidParamMap = ParamValidator.validConfig(validatorConfig);
    if (!invalidParamMap.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (Map.Entry<String, String> entry : invalidParamMap.entrySet()) {
        sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(LINE_SEPARATOR);
      }
      sb.append(configPath).append(LINE_SEPARATOR);
      List<String> fieldNameList =
          Arrays.asList(
              "nodePublicKey",
              "nodeAddress",
              "nodePort",
              "nodeRpcPort",
              //"nodeName",
              //"externalId",
              //"webSite",
              //"benefitAddress",
              //"details",
              "chainId",
              "blsPubKey",
              "certificate");
      for (Map.Entry<String, String> entry : invalidParamMap.entrySet()) {
        if (fieldNameList.contains(entry.getKey())) {
          sb.append("Invalid ").append(entry.getKey()).append(LINE_SEPARATOR);
        }
      }
      throw new ParameterException(sb.toString());
    }
    // 判断收益地址是否是目标链地址
    /*if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),validatorConfig.getBenefitAddress())){
      throw new ParameterException("benefitAddress["+validatorConfig.getBenefitAddress()+"] is not a legal address of chain["+CLIENT_CONFIG.getTargetChainId()+"]");
    }*/
  }
}
