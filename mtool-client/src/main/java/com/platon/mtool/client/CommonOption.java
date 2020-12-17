package com.platon.mtool.client;

import com.beust.jcommander.Parameter;

/**
 * 通用选项
 *
 * <p>Created by liyf.
 */
public class CommonOption {

  @Parameter(
      names = {"--help"},
      help = true,
      description = "Show command help")
  private boolean help;

  public boolean isHelp() {
    return help;
  }

  public void setHelp(boolean help) {
    this.help = help;
  }
}
