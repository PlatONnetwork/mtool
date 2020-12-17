package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.options.restricting.RestrictingConfig;
import com.platon.mtool.client.options.restricting.RestrictingConfigConverter;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RestrictingConfigConverterTest {
    private RestrictingConfigConverter converter = new RestrictingConfigConverter("--file");

    @Test
    void convert() {

        RestrictingConfig restrictingConfig = null;
        try {
            restrictingConfig = converter.convert(Paths.get(ClassLoader.getSystemResource("restricting_plans.json").toURI()).toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assertEquals(3, restrictingConfig.getPlans().length);
    }

    @Test
    void convertException() {
        ParameterException exception =
                assertThrows(ParameterException.class, () -> converter.convert("/path/nofound"));
        assertEquals("\"--file\": /path/nofound (No such file )", exception.getMessage());
    }
}
