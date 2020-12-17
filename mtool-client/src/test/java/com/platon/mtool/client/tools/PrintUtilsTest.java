package com.platon.mtool.client.tools;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class PrintUtilsTest {

  @Test
  void echo() {
    PrintUtils.echo("test");
    assertTrue(true);
  }

  @Test
  void testEcho() {
    PrintUtils.echo(System.out, "test");
    assertTrue(true);
  }

  @Test
  void testEcho1() {
    PrintUtils.echo("test%s", "test");
    assertTrue(true);
  }

  @Test
  void testEcho2() {
    PrintUtils.echo(
        new PrintWriter(new OutputStreamWriter(System.out, StandardCharsets.UTF_8), true), "test");
    assertTrue(true);
  }
}
