package com.platon.mtool.common.logger;

import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.utils.LogUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Created by liyf. */
class LogTest {

  private static final Logger logger = LoggerFactory.getLogger(LogTest.class);

  @Test
  void success() {
    ValidatorConfig config = new ValidatorConfig();
    config.setNodePort(8080);
    Map<String, Object> map = new HashMap<>();
    map.put("mapKey1", "mapValue");
    map.put("mapKey2", config);
    Log log =
        Log.newBuilder()
            .op("testOp")
            .msg("testMsg")
            .kv("key1", "value1")
            .kv("key2", config)
            .kv("key3", map)
            .build();
    System.out.println(log.getTextMsg());
    assertEquals(
        "### op: testOp|msg: testMsg|kv: {key1=value1, key2={\"nodePort\":8080}, "
            + "key3={\"mapKey2\":{\"nodePort\":8080},\"mapKey1\":\"mapValue\"}}|",
        log.getTextMsg());
  }

  @Test
  void logUtilSuccess() {
    LogUtils.info(logger, () -> Log.newBuilder().msg("haha"));
    assertTrue(true);
  }
}
