package com.platon.mtool.client.options.validator;

import com.beust.jcommander.IParameterValidator2;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import com.platon.mtool.common.utils.AddressUtil;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

public class AddressValidator implements IParameterValidator2 {
    @Override
    public void validate(String name, String value, ParameterDescription pd) throws ParameterException {
        // 检查是否是目标链的合法地址
        if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),value)){
            throw new ParameterException(value+" is not a legal address of chainID["+CLIENT_CONFIG.getTargetChainId()+"]");
        }
    }

    @Override
    public void validate(String name, String value) throws ParameterException {
        // 检查是否是目标链的合法地址
        if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),value)){
            throw new ParameterException(value+" is not a legal address of chainID["+CLIENT_CONFIG.getTargetChainId()+"]");
        }
    }
}
