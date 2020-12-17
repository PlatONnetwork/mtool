package com.platon.mtool.client.options.restricting;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.mtool.client.converter.PathConverter;
import com.platon.mtool.client.options.validator.AddressValidator;
import org.apache.commons.lang3.StringUtils;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class RestrictingConfigConverter extends BaseConverter<RestrictingConfig> {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    PathConverter pathConverter = new PathConverter(getOptionName());

    public RestrictingConfigConverter(String optionName) {
        super(optionName);
    }

    @Override
    public RestrictingConfig convert(String planFile) {
        Path path = pathConverter.convert(planFile);
        String pathStr = path.toAbsolutePath().toString();

        RestrictingConfig restrictingConfig;
        //AddressValidator
        try (InputStream is = Files.newInputStream(path)) {
            restrictingConfig = JSON.parseObject(is, RestrictingConfig.class);
        } catch (NoSuchFileException e) {
            throw new ParameterException("restricting file not found");
        } catch (Exception e) {
            throw new ParameterException("restricting file error");
        }

        if(restrictingConfig==null || StringUtils.isBlank(restrictingConfig.getAccount())){
            throw new ParameterException("account in restricting file error");
        }

        /*if (restrictingConfig.getPlans()==null || restrictingConfig.getPlans().length==0 || restrictingConfig.getPlans().length>36){
            throw new ParameterException("plans in restricting file error");
        }*/

        AddressValidator addressValidator = new AddressValidator();
        addressValidator.validate("account", restrictingConfig.getAccount());

        return restrictingConfig;
    }
}
