package com.platon.mtool.common.web3j;

import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolException;
import com.platon.protocol.Web3j;
import com.platon.protocol.http.HttpService;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * web3j工具类，生成的web3j支持https
 *
 * <p>Created by liyf.
 */
public class Web3jUtil {

  private static final Logger logger = LoggerFactory.getLogger(Web3jUtil.class);

  private Web3jUtil() {}

  public static Web3j getFromConfig(ValidatorConfig config) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    configureLogging(builder);
    if (config.getNodeAddress().contains("https://")
        && StringUtils.isNotEmpty(config.getCertificate())) {
      try {
        configSsl(builder, config.getCertificate());
      } catch (KeyManagementException
          | NoSuchAlgorithmException
          | CertificateException
          | KeyStoreException
          | IOException e) {
        throw new MtoolException("can't not support ssl connection", e);
      }
    }

    HttpService httpService = createHttpService(builder.build(), config);
    return Web3j.build(httpService);
  }

  private static HttpService createHttpService(OkHttpClient client, ValidatorConfig config) {

    HttpService httpService =
        new HttpService(config.getNodeAddress() + ":" + config.getNodeRpcPort(), client, false);

    Pattern pattern = Pattern.compile("http[s]?://(.*):(.*)@.*");
    Matcher matcher = pattern.matcher(config.getNodeAddress());
    if (matcher.find()) {
      String credentials = Credentials.basic(matcher.group(1), matcher.group(2));
      httpService.addHeader("Authorization", credentials);
    }
    return httpService;
  }

  private static void configureLogging(OkHttpClient.Builder builder) {
    if (logger.isDebugEnabled()) {
      HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger::debug);
      logging.setLevel(HttpLoggingInterceptor.Level.BODY);
      builder.addInterceptor(logging);
    }
  }

  private static void configSsl(OkHttpClient.Builder builder, String certificate)
      throws KeyManagementException, NoSuchAlgorithmException, CertificateException,
          KeyStoreException, IOException {
    X509TrustManager trustManager = createTrustManager(certificate);
    SSLSocketFactory sslSocketFactory = createSslSocketFactory(trustManager);
    builder.sslSocketFactory(sslSocketFactory, trustManager);
  }

  private static X509TrustManager createTrustManager(String certificatePath)
      throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException {
    Path path = Paths.get(certificatePath);
    if (!path.toFile().exists()) {
      throw new MtoolException("certificate not found");
    }
    // 加载证书
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    X509Certificate certificate =
        (X509Certificate) certificateFactory.generateCertificate(Files.newInputStream(path));

    // 创建空keystore
    char[] password = "".toCharArray();
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, password);
    keyStore.setCertificateEntry("mtool", certificate);

    // 使用包含自签证书信息的keyStore去构建一个X509TrustManager
    TrustManagerFactory trustManagerFactory =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    trustManagerFactory.init(keyStore);
    TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
    if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
      throw new MtoolException("can't create X509TrustManager");
    }
    return (X509TrustManager) trustManagers[0];
  }

  private static SSLSocketFactory createSslSocketFactory(X509TrustManager trustManager)
      throws NoSuchAlgorithmException, KeyManagementException {
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    // 使用构建出的trustManger初始化SSLContext对象
    sslContext.init(null, new TrustManager[] {trustManager}, null);
    // 获得sslSocketFactory对象
    return sslContext.getSocketFactory();
  }
}
