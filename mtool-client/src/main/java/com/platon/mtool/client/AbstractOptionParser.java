package com.platon.mtool.client;

import com.beust.jcommander.JCommander;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 选项解析器抽象类
 *
 * <p>Created by liyf.
 */
public abstract class AbstractOptionParser {

  private final JCommander jCommander;
  private final RootOption rootOption;

  protected AbstractOptionParser(String programName) {
    rootOption = new RootOption();
    jCommander =
        JCommander.newBuilder()
            .resourceBundle(ResourceBundle.getBundle("MessageBundle", Locale.US))
            .addObject(rootOption)
            .build();
    buildOption(programName, jCommander);
  }

  public static String getCommandName(JCommander jCommander) {
    return (jCommander.getParsedCommand() != null) ? jCommander.getParsedCommand() : "";
  }

  public static JCommander getSubCommander(JCommander jCommander) {
    String parsedCommand = jCommander.getParsedCommand();
    return jCommander.getCommands().get(parsedCommand);
  }

  public RootOption getRootOption() {
    return rootOption;
  }

  public JCommander getjCommander() {
    return jCommander;
  }

  public String getCommandName() {
    return (jCommander.getParsedCommand() != null) ? jCommander.getParsedCommand() : "";
  }

  public String getSubCommandName() {
    JCommander subCommander = getSubCommander();
    return (subCommander.getParsedCommand() != null) ? subCommander.getParsedCommand() : "";
  }

  public JCommander getSubCommander() {
    return getSubCommander(jCommander);
  }

  public abstract void buildOption(String programName, JCommander commander);

  public void parse(String[] args) {
    jCommander.parse(args);
  }
}
