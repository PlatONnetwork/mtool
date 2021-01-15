package com.platon.mtool.client.tools;

import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.parameters.NetworkParameters;
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
            CLIENT_CONFIG.host = p.getProperty("host", "");
            CLIENT_CONFIG.port = Integer.valueOf(p.getProperty("port", "80"));
            CLIENT_CONFIG.chainId = Long.valueOf(p.getProperty("chainId"));
            CLIENT_CONFIG.hrp = p.getProperty("hrp");
            // 使用配置中的链ID覆盖SDK默认值(如果有配置)
            NetworkParameters.init(CliConfigUtils.CLIENT_CONFIG.chainId, CliConfigUtils.CLIENT_CONFIG.hrp);
        } catch (IOException e) {
            logger.error("Unable to read file {}", ResourceUtils.getRootPath(), e);
            throw new MtoolClientException("Unable to read file " + ResourceUtils.getKeystorePath() + ":" + e.getMessage());
        } catch (Exception e1) {
            logger.error("Check the values in config file. {}", ResourceUtils.getRootPath(), e1);
            throw new MtoolClientException("Config file error. " + ResourceUtils.getKeystorePath() + " : " + e1.getMessage());
        }
    }

    private CliConfigUtils() {
    }

    public static class MtoolClientConfig {

        private String host;
        private Integer port;
        private Long chainId;
        private String hrp;


        public String getHost() {
            return host;
        }

        public Integer getPort() {
            return port;
        }

        public Long getChainId() {
            return chainId;
        }

        public void setChainId(Long chainId) {
            this.chainId = chainId;
        }

        public String getHrp() {
            return hrp;
        }

        public void setHrp(String hrp) {
            this.hrp = hrp;
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
