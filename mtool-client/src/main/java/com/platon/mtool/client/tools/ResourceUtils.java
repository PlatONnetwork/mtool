package com.platon.mtool.client.tools;

import com.platon.mtool.common.exception.MtoolClientException;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 资源路径工具类， 封装了mtool解压后默认的文件夹路径
 *
 * <p>Created by liyf.
 */
public abstract class ResourceUtils {
  /** 钱包文件夹 */
  private static final String KEYSTORE_DIR_NAME = "keystore" + File.separator;
  /** 奖励文件文件夹 */
  private static final String REWARD_DATA_DIR_NAME = "reward_data" + File.separator;
  /** 验证人文件夹 */
  private static final String VALIDATOR_DIR_NAME = "validator" + File.separator;
  /** 交易明细文件夹 */
  private static final String TRANSACTION_DETAILS_DIR_NAME = "transaction_details" + File.separator;
  /** 签名文件文件夹 */
  private static final String TRANSACTION_SIGNATURE_DIR_NAME =
      "transaction_signature" + File.separator;

  /** 根文件夹 */
  private static final String ROOT_DIR_NAME = "." + File.separator;

  private static Path keystorePath;
  private static Path rewardDataPath;
  private static Path validatorPath;
  private static Path transactionDetailsPath;
  private static Path transactionSignaturePath;
  private static Path rootPath;
  private static URL jarPath =
      ResourceUtils.class.getProtectionDomain().getCodeSource().getLocation();

  private ResourceUtils() {}

  public static Path getResource(String name) {
    try {
      return Paths.get(ClassLoader.getSystemResource(name).toURI());
    } catch (URISyntaxException e) {
      throw new MtoolClientException("getResource error");
    }
  }

  public static Path getKeystorePath() {
    if (keystorePath == null) {
      try {
        Path path = Paths.get(jarPath.toURI()).getParent().getParent().resolve(KEYSTORE_DIR_NAME);
        if (!path.toFile().exists()) {
          Files.createDirectories(path);
        }
        keystorePath = path;
      } catch (Exception e) {
        throw new MtoolClientException("getKeystorePath error");
      }
    }
    return keystorePath;
  }

  public static Path getRewardDataPath() {
    if (rewardDataPath == null) {
      try {
        Path path =
            Paths.get(jarPath.toURI()).getParent().getParent().resolve(REWARD_DATA_DIR_NAME);
        if (!path.toFile().exists()) {
          Files.createDirectories(path);
        }
        rewardDataPath = path;
      } catch (Exception e) {
        throw new MtoolClientException("getRewardDataPath error");
      }
    }
    return rewardDataPath;
  }

  public static Path getValidatorPath() {
    if (validatorPath == null) {
      try {
        Path path = Paths.get(jarPath.toURI()).getParent().getParent().resolve(VALIDATOR_DIR_NAME);
        if (!path.toFile().exists()) {
          Files.createDirectories(path);
        }
        validatorPath = path;
      } catch (Exception e) {
        throw new MtoolClientException("getValidatorPath error");
      }
    }
    return validatorPath;
  }

  public static Path getTransactionDetailsPath() {
    if (transactionDetailsPath == null) {
      try {
        Path path =
            Paths.get(jarPath.toURI())
                .getParent()
                .getParent()
                .resolve(TRANSACTION_DETAILS_DIR_NAME);
        if (!path.toFile().exists()) {
          Files.createDirectories(path);
        }
        transactionDetailsPath = path;
      } catch (Exception e) {
        throw new MtoolClientException("getTransactionDetailsPath error");
      }
    }
    return transactionDetailsPath;
  }

  public static Path getTransactionSignaturePath() {
    if (transactionSignaturePath == null) {
      try {
        Path path =
            Paths.get(jarPath.toURI())
                .getParent()
                .getParent()
                .resolve(TRANSACTION_SIGNATURE_DIR_NAME);
        if (!path.toFile().exists()) {
          Files.createDirectories(path);
        }
        transactionSignaturePath = path;
      } catch (Exception e) {
        throw new MtoolClientException("getTransactionSignaturePath error");
      }
    }
    return transactionSignaturePath;
  }

  public static Path getRootPath() {
    if (rootPath == null) {
      try {
        rootPath = Paths.get(jarPath.toURI()).getParent().getParent().resolve(ROOT_DIR_NAME);
      } catch (Exception e) {
        throw new MtoolClientException("getRootPath error");
      }
    }
    return rootPath;
  }
}
