package com.platon.mtool.client.options.validator;

import com.beust.jcommander.IParameterValidator2;
import com.beust.jcommander.ParameterDescription;
import com.beust.jcommander.ParameterException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;

public class DelegateRewardPercentValidator implements IParameterValidator2 {
    @Override
    public void validate(String name, String value, ParameterDescription pd) throws ParameterException {
        if(StringUtils.isBlank(value)){
            throw new ParameterException(value+" is invalid");
        }else{
            BigInteger percent = new BigInteger(value);
            if (percent.signum()<0 || percent.intValue()>10000){
                throw new ParameterException( name + " value( "  + value +" ) is out of range [0, 10000]");
            }
        }
    }

    @Override
    public void validate(String name, String value) throws ParameterException {

    }
}
