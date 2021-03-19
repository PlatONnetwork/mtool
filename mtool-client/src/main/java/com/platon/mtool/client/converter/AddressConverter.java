package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.bech32.Bech32;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.parameters.NetworkParameters;

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
    /*if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getChainId(),value)){
      throw new ParameterException(value+" is not a legal address of chain["+CLIENT_CONFIG.getChainId()+"]");
    }*/
    if(Bech32.checkBech32Addr(value)){
      return value;
    }
    PrintUtils.echo(value+" is not a legal address of dest chainID:" + NetworkParameters.getChainId());
    throw new ParameterException("Invalid " + getOptionName().replace("--", "").trim());
  }
}
