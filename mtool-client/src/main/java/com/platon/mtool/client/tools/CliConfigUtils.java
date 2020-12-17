package com.platon.mtool.client.tools;

import com.platon.mtool.common.exception.MtoolClientException;
import com.alaya.parameters.NetworkParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

/**
 * mtool配置文件工具，用来指向服务器端口和路径
 *
 * <p>Created by liyf.
 */
public class CliConfigUtils {
  private static final Logger logger = LoggerFactory.getLogger(CliConfigUtils.class);
  public static final MtoolClientConfig CLIENT_CONFIG = new MtoolClientConfig();
  static {
    Properties p = new Properties();
    try {
      p.load(Files.newInputStream(ResourceUtils.getRootPath().resolve("config.properties")));
      CLIENT_CONFIG.host=p.getProperty("host", "");
      CLIENT_CONFIG.port=Integer.valueOf(p.getProperty("port", "80"));
      CLIENT_CONFIG.targetChainId=Long.valueOf(p.getProperty("chainId", String.valueOf(NetworkParameters.TestNetParams.getChainId())));
      CLIENT_CONFIG.mainNetChainId=Long.valueOf(p.getProperty("mainNetChainId", String.valueOf(NetworkParameters.MainNetParams.getChainId())));
      CLIENT_CONFIG.testNetChainId=Long.valueOf(p.getProperty("testNetChainId", String.valueOf(NetworkParameters.TestNetParams.getChainId())));
      // 使用配置中的链ID覆盖SDK默认值(如果有配置)
      NetworkParameters.MainNetParams.setChainId(CLIENT_CONFIG.mainNetChainId);
      NetworkParameters.TestNetParams.setChainId(CLIENT_CONFIG.testNetChainId);
    } catch (IOException e) {
      logger.error("Unable to read file {}", ResourceUtils.getRootPath(), e);
      throw new MtoolClientException("Unable to read file "+ResourceUtils.getKeystorePath()+":"+e.getMessage());
    }
  }

  private CliConfigUtils() {}

  public static class MtoolClientConfig {

    private String host;
    private Integer port;
    private Long targetChainId;
    private Long mainNetChainId;
    private Long testNetChainId;

    public String getHost() {
      return host;
    }
    public Integer getPort() {
      return port;
    }

    public Long getTargetChainId() {
      return targetChainId;
    }

    public Long getMainNetChainId() {
      return mainNetChainId;
    }

    public Long getTestNetChainId() {
      return testNetChainId;
    }

    public String getAddress() {
      if (host.contains("http")) {
        return host + ":" + port;
      } else {
        return "http://" + host + ":" + port;
      }
    }
  }
}
