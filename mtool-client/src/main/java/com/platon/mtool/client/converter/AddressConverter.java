package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.common.utils.AddressUtil;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

/**
 * 路径转换器
 *
 * <p>Created by liyf.
 */
public class AddressConverter extends BaseConverter<String> {

  public AddressConverter(String optionName) {
    super(optionName);
  }

  @Override
  public String convert(String value) {
    // 检查是否是目标链的合法地址
    if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),value)){
      throw new ParameterException(value+" is not a legal address of chain["+CLIENT_CONFIG.getTargetChainId()+"]");
    }
    return value;
  }
}
