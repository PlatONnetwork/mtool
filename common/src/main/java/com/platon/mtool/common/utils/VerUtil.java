package com.platon.mtool.common.utils;

import com.platon.mtool.common.exception.MtoolException;
import com.platon.mtool.common.logger.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本号转换工具
 *
 * <p>Created by liyf.
 */
public abstract class VerUtil {

  private static final Logger logger = LoggerFactory.getLogger(VerUtil.class);
  private static Pattern pattern = Pattern.compile("^(\\d+)\\.(\\d+)\\.(\\d+)$");

  private VerUtil() {}

  public static BigInteger toInteger(String version) {
    Matcher matcher = pattern.matcher(version);
    if (matcher.find()) {
      LogUtils.info(
          logger,
          () ->
              Log.newBuilder()
                  .kv("version", matcher.group(1))
                  .kv("lite", matcher.group(2))
                  .kv("patch", matcher.group(3)));
      int ver = Byte.parseByte(matcher.group(1)) << 16 & 0x7fffffff;
      int lite = Byte.parseByte(matcher.group(2)) << 8 & 0x7fffffff;
      int patch = Byte.parseByte(matcher.group(3)) & 0x7fffffff;
      int id = ver | lite | patch;
      return BigInteger.valueOf(id);
    } else {
      throw new MtoolException("version is invalid");
    }
  }

  public static String toVersion(BigInteger version) {
    int v = version.intValue();
    int ver = v >> 16 & 0x0000ffff;
    int lite = v >> 8 & 0x000000ff;
    int patch = v & 0x000000ff;
    return String.format("%s.%s.%s", ver, lite, patch);
  }
}
