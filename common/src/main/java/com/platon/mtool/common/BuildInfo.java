package com.platon.mtool.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * mtool客户端打包版本信息
 *
 * <p>Created by liyf.
 */
public final class BuildInfo {

  private static final Logger logger = LoggerFactory.getLogger(BuildInfo.class);

  private static final String PROPERTIES_FILE = "build-info.properties";

  private BuildInfo() {
    // Prevent instantiation - all methods are static
  }

  /**
   * version.
   *
   * @return version
   */
  public static String version() {
    try (InputStream in = BuildInfo.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
      if (in != null) {
        Properties p = new Properties();
        p.load(in);
        String version = p.getProperty("version");

        if (StringUtils.isNotBlank(version)) {
          return version;
        }
      }
    } catch (final IOException e) {
      logger.error("Unable to read file {}", PROPERTIES_FILE, e);
    }

    return "Undefined";
  }

  /**
   * version info.
   *
   * @return version info
   */
  public static String info() {
    try {
      try (InputStream in = BuildInfo.class.getResourceAsStream("/" + PROPERTIES_FILE)) {
        if (in != null) {
          Properties p = new Properties();
          p.load(in);
          String version = p.getProperty("version");
          String timestamp = p.getProperty("timestamp");
          String revision = p.getProperty("revision");
          String sdkVersion = p.getProperty("sdkVersion");
          return String.format(
              "version: %s%ntimestamp: %s%nrevision: %s%nsdk version: %s",
              version, timestamp, revision, sdkVersion);
        }
      }
    } catch (final IOException e) {
      logger.error("Unable to read file {}", PROPERTIES_FILE, e);
    }

    return "Undefined";
  }
}
