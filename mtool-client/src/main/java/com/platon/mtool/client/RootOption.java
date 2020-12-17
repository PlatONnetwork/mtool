package com.platon.mtool.client;

import com.beust.jcommander.Parameter;

/**
 * 通用根选项
 *
 * <p>Created by liyf.
 */
public class RootOption {

  @Parameter(
      names = {"--help", "-h"},
      help = true,
      description = "Show command help")
  private boolean help;

  @Parameter(
      names = {"--version", "-v"},
      help = true,
      description = "Show version")
  private boolean showVersion;

  public boolean isHelp() {
    return help;
  }

  public void setHelp(boolean help) {
    this.help = help;
  }

  public boolean isShowVersion() {
    return showVersion;
  }

  public void setShowVersion(boolean showVersion) {
    this.showVersion = showVersion;
  }
}
