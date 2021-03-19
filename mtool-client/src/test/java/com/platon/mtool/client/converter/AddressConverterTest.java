package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.tools.CliConfigUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddressConverterTest {
    private AddressConverter converter = new AddressConverter("--address");

    private static String addr = "lat196278ns22j23awdfj9f2d4vz0pedld8anl5k3a";

    @BeforeAll
    static void Setup(){
        CliConfigUtils.loadProperties();
    }
    @Test
    void convert() {
        String address = converter.convert(addr);
        assertEquals(address, addr);
    }

    @Test
    void convertParameterException() {
        ParameterException exception = assertThrows(ParameterException.class, () -> converter.convert("atp1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqy4tn65x"));
        assertEquals("Invalid address", exception.getMessage());
    }

    @Test
    void convertRuntimeException() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> converter.convert("lat196278ns22j23awdfj9f2d4vz0pedld8anl5k3b"));
        assertTrue(exception.getMessage().contains("decode Bech32 address error"));
    }
}
