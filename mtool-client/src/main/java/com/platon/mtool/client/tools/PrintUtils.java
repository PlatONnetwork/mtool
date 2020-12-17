package com.platon.mtool.client.tools;

import java.io.Console;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;
import java.util.Scanner;

/**
 * 控制台打印工具类
 *
 * <p>Created by liyf.
 */
public abstract class PrintUtils {

  private PrintUtils() {}

  public static void echo(String format, Object... args) {
    echo(String.format(format, args));
  }

  /**
   * echo message to console.
   *
   * @param message message
   */
  public static void echo(String message) {
    echo(System.out, message);
  }

  public static void echo(final PrintStream out, String message) {
    echo(new PrintWriter(new OutputStreamWriter(out), true), message);
  }

  public static void echo(final PrintWriter out, String message) {
    out.println(message);
  }

  /**
   * read password from console.
   *
   * @param promote promote message
   * @return password char array
   */
  public static char[] readPassword(String promote) {
    if (isJunitTest()) {
      echo("junit test set password 123456");
      return new char[] {'1', '2', '3', '4', '5', '6'};
    }
    Console console = System.console();
    if (console == null) {
      echo(System.err, "ide can't support console, enter debug mode and echo input");
      echo(promote);
      Scanner scanner = new Scanner(System.in);
      String password = scanner.next();
      return password.toCharArray();
    } else {
      return console.readPassword(promote);
    }
  }

  public static String interact(String promote) {
    return interact(promote, ".*");
  }

  /**
   * read user interact input from console.
   *
   * @param promote promote message
   * @param regex regex message from user interact input.
   * @return user interact input
   */
  public static String interact(String promote, String regex) {
    return interact(promote, regex, false);
  }

  /**
   * read user interact input from console.
   *
   * @param promote promote message
   * @param regex regex message from user interact input.
   * @param ignoreCase ignore capital case
   * @return user interact input
   */
  public static String interact(String promote, String regex, boolean ignoreCase) {
    echo(promote);
    if (isJunitTest()) {
      String retValue = null;
      if (regex.equals("y|n")) {
        retValue = "y";
      } else if (regex.equals("yes|no")) {
        retValue = "yes";
      } else if (promote.equals("Adjust Distribution amount (ATP):")) {
        retValue = "5000000.000001";
      }
      echo("junit test set input: " + retValue);
      return retValue;
    }
    Scanner scanner = new Scanner(System.in);
    String input = scanner.nextLine();
    if (ignoreCase) {
      input = input.toLowerCase();
      regex = regex.toLowerCase();
    }
    if (input.matches(regex)) {
      return input;
    } else {
      return interact(promote, regex, ignoreCase);
    }
  }

  /**
   * whether runtime is running for junit.
   *
   * @return boolean
   */
  public static boolean isJunitTest() {
    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    List<String> arguments = runtimeMxBean.getInputArguments();
    return arguments.contains("-DjunitTest");
  }
}
