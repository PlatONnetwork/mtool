package com.platon.mtool.client.test;


import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.junit.jupiter.api.Assertions;



public class JCommanderTest {
    @Parameter(names = { "--log"}, required = true, description = "Level of verbosity")
    public Integer log = 1;

    @Parameter(names = "--debug", required = true, description = "Debug mode")
    public String debug = "info";


    public static void main(String args[]){
        JCommanderTest jct = new JCommanderTest();
        String[] argv = { "--log", "2", "--debug"};
        JCommander.newBuilder()
                .addObject(jct)
                .build()
                .parse(argv);

        Assertions.assertEquals(2, jct.log.intValue());
        Assertions.assertEquals("error", jct.debug);
    }
}
