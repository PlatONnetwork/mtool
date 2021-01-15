package com.platon.mtool.client.options.validator;

import com.beust.jcommander.IParameterValidator2;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import com.platon.bech32.Bech32;

public class AddressValidator implements IParameterValidator2 {
    @Override
    public void validate(String name, String value, ParameterDescription pd) throws ParameterException {
        // 检查是否是目标链的合法地址
        //if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),value)){
        if(!Bech32.checkBech32Addr(value)){
            throw new ParameterException(value+" is not a legal address of dest chain");
        }
    }

    @Override
    public void validate(String name, String value) throws ParameterException {
        // 检查是否是目标链的合法地址
        //if(!AddressUtil.isValidTargetChainAccountAddress(CLIENT_CONFIG.getTargetChainId(),value)){
        if(!Bech32.checkBech32Addr(value)){
            throw new ParameterException(value+" is not a legal address of dest chain");
        }
    }
}
