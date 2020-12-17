package com.platon.mtool.client.tools;

import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.asynchttpclient.Dsl.asyncHttpClient;
import static org.asynchttpclient.Dsl.config;

/**
 * http请求客户端
 *
 * <p>Created by liyf.
 */
public abstract class HttpClient {

  private static final Logger logger = LoggerFactory.getLogger(HttpClient.class);

  private HttpClient() {}

  public static Response post(Request request) {
    try (AsyncHttpClient asyncHttpClient =
        asyncHttpClient(
            config()
                .setConnectTimeout(100)
                .setReadTimeout(20 * 60 * 1000) // 20 minutes wait to download file
                .setRequestTimeout(10 * 60 * 1000) // 10 minutes
            )) {
      // 插入uuid， 用来跟踪客户端和服务端的日志， 相同uuid必定是同一个操作产生的
      String uuid = MDC.get("uuid");
      if (logger.isInfoEnabled()) {
        LogUtils.info(
            logger,
            () -> Log.newBuilder().msg("post").kv("url", request.getUrl()).kv("uuid", uuid));
      }
      if (StringUtils.isNotEmpty(uuid)) {
        request.getHeaders().set("uuid", uuid);
      }
      return asyncHttpClient.executeRequest(request).get();
    } catch (IOException | ExecutionException e) {
      logger.error("post error, check if the mtool-server is started", e);
      throw new MtoolClientException("post error, check if the mtool-server is started");
    } catch (InterruptedException e) {
      logger.error("post InterruptedException", e);
      Thread.currentThread().interrupt();
      throw new MtoolClientException("post InterruptedException");
    }
  }
}
