package com.platon.mtool.client.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.client.tools.HttpClient;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.entity.http.PeriodInfo;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.asynchttpclient.Request;
import org.asynchttpclient.RequestBuilder;
import org.asynchttpclient.Response;
import org.asynchttpclient.request.body.multipart.FilePart;
import org.asynchttpclient.request.body.multipart.InputStreamPart;
import org.asynchttpclient.request.body.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigInteger;
import java.util.List;

import static com.platon.mtool.client.ClientConsts.SUCCESS;
import static com.platon.mtool.client.ClientConsts.URL_CLAWLER_DEADLINE_PERIOD;
import static com.platon.mtool.client.ClientConsts.URL_CLAWLER_TO_DEADLINE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_DIVIDEPERIOD;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_DIVIDE_RESULT;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_GENFILE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_GENTOTALFILE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_VALID_BLOCK_NUMBER;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_VALID_DIVIDE;
import static com.platon.mtool.client.ClientConsts.URL_SYSTEM_PREPARE_WEB3J;
import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;
import static org.asynchttpclient.Dsl.post;

/**
 * http请求服务， 单例
 *
 * <p>Created by liyf.
 */
public class RequestService {

  private static final Logger logger = LoggerFactory.getLogger(RequestService.class);

  private static final String baseUrl = CLIENT_CONFIG.getAddress();

  public static RequestService singleton() {
    return RequestServiceHolder.instance;
  }

  // 奖励发放结束回传文件结果
  public void postDivideResult(String nodeId, File resultFile) {
    Request r =
        post(baseUrl + URL_REWARD_DIVIDE_RESULT)
            .addBodyPart(new StringPart("nodeId", nodeId))
            .addBodyPart(new FilePart("rewardFile", resultFile))
            .build();
    final Response resp = HttpClient.post(r);

    if (!resp.getResponseBody().equals(SUCCESS)) {
      throw new MtoolClientException(resp.getResponseBody());
    }
  }

  // web3j连接预生成
  public void postPrepareWeb3j(ValidatorConfig validatorConfig) {
    RequestBuilder builder =
        post(baseUrl + URL_SYSTEM_PREPARE_WEB3J)
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"));

    if (StringUtils.isNotEmpty(validatorConfig.getCertificate())) {
      builder.addBodyPart(new FilePart("certificate", new File(validatorConfig.getCertificate())));
    }

    Request r = builder.build();

    final Response resp = HttpClient.post(r);
    LogUtils.info(
        logger, () -> Log.newBuilder().kv("prepareWeb3j response", resp.getResponseBody()));
    if (!resp.getResponseBody().equals(SUCCESS)) {
      throw new MtoolClientException(resp.getResponseBody());
    }
  }

  // 获取有效的周期
  // 分割期数
  public PeriodInfo postValidBlockNumber(
      BigInteger startBlock, BigInteger endBlock, ValidatorConfig validatorConfig) {
    RequestBuilder builder =
        post(baseUrl + URL_REWARD_VALID_BLOCK_NUMBER)
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"));
    if (startBlock != null) {
      builder.addBodyPart(new StringPart("start_block", startBlock.toString()));
    }
    if (endBlock != null) {
      builder.addBodyPart(new StringPart("end_block", endBlock.toString()));
    }
    Request r = builder.build();
    final Response divideResponse = HttpClient.post(r);
    LogUtils.info(
        logger, () -> Log.newBuilder().kv("period response", divideResponse.getResponseBody()));
    PeriodInfo periodInfo;
    try {
      periodInfo = JSON.parseObject(divideResponse.getResponseBody(), PeriodInfo.class);
    } catch (Exception e) {
      logger.error("period parse error", e);
      throw new MtoolClientException(divideResponse.getResponseBody());
    }
    return periodInfo;
  }

  // 分割期数
  public List<PeriodInfo> postDividePeriod(
      BigInteger startBlock, BigInteger endBlock, ValidatorConfig validatorConfig) {

    RequestBuilder builder =
        post(baseUrl + URL_REWARD_DIVIDEPERIOD)
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"));
    if (startBlock != null) {
      builder.addBodyPart(new StringPart("start_block", startBlock.toString()));
    }
    if (endBlock != null) {
      builder.addBodyPart(new StringPart("end_block", endBlock.toString()));
    }
    Request r = builder.build();
    final Response divideResponse = HttpClient.post(r);
    LogUtils.info(
        logger, () -> Log.newBuilder().kv("period response", divideResponse.getResponseBody()));
    List<PeriodInfo> pairList;
    try {
      pairList =
          JSON.parseObject(
              divideResponse.getResponseBody(), new TypeReference<List<PeriodInfo>>() {});
    } catch (Exception e) {
      logger.error("period list parse error", e);
      throw new MtoolClientException(divideResponse.getResponseBody());
    }
    return pairList;
  }

  // 获取每个周期的奖励分配文件
  public byte[] postGenFile(
      Integer settlePeriod, ValidatorConfig validatorConfig, File rewardConfigFile) {
    Request r =
        post(baseUrl + URL_REWARD_GENFILE)
            .addBodyPart(new StringPart("settle_period", settlePeriod.toString()))
            .addBodyPart(new FilePart("reward_config", rewardConfigFile))
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"))
            .build();
    final Response res = HttpClient.post(r);
    if (StringUtils.isEmpty(res.getHeader("Content-disposition"))) {
      logger.error("period can't generate ({})", settlePeriod);
      throw new MtoolClientException(res.getResponseBody());
    }
    return res.getResponseBodyAsBytes();
  }

  // 导出汇总文件
  public byte[] postGenTotalFile(
      BigInteger startBlock,
      BigInteger endBlock,
      BigInteger adjustAmount,
      ValidatorConfig validatorConfig,
      File rewardConfigFile) {
    RequestBuilder builder =
        post(baseUrl + URL_REWARD_GENTOTALFILE)
            .addBodyPart(new StringPart("start_block", startBlock.toString()))
            .addBodyPart(new StringPart("end_block", endBlock.toString()))
            .addBodyPart(new FilePart("reward_config", rewardConfigFile))
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"));

    if (adjustAmount != null) {
      builder.addBodyPart(new StringPart("adjust_amount", adjustAmount.toString()));
    }

    Request request = builder.build();
    final Response resp = HttpClient.post(request);
    if (StringUtils.isEmpty(resp.getHeader("Content-disposition"))) {
      logger.error("period can't generate ({},{})", startBlock, endBlock);
      throw new MtoolClientException(resp.getResponseBody());
    }
    return resp.getResponseBodyAsBytes();
  }

  // 服务器查看是否终态 DONE, FAIL
  public void postValidDivide(String nodeId, BigInteger startBlock, BigInteger endBlock) {
    Request r =
        post(baseUrl + URL_REWARD_VALID_DIVIDE)
            .addFormParam("nodeId", nodeId)
            .addFormParam("startBlockNumber", startBlock.toString())
            .addFormParam("endBlockNumber", endBlock.toString())
            .build();
    Response resp = HttpClient.post(r);
    if (!resp.getResponseBody().equals(SUCCESS)) {
      throw new MtoolClientException(resp.getResponseBody());
    }
  }

  // 爬取数据
  public void postCrawlerData(BigInteger endBlockNumber, ValidatorConfig validatorConfig) {
    Request r =
        post(baseUrl + URL_CLAWLER_TO_DEADLINE)
            .addBodyPart(new StringPart("end_block", endBlockNumber.toString()))
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"))
            .build();
    Response resp = HttpClient.post(r);
    if (!resp.getResponseBody().equals(SUCCESS)) {
      throw new MtoolClientException(resp.getResponseBody());
    }
  }

  // 分割抓取期数
  public List<PeriodInfo> postDeadlinePeriod(
      BigInteger endBlockNumber, ValidatorConfig validatorConfig) {

    RequestBuilder builder =
        post(baseUrl + URL_CLAWLER_DEADLINE_PERIOD)
            .addBodyPart(
                new InputStreamPart(
                    "validator_config",
                    new ByteArrayInputStream(JSON.toJSONString(validatorConfig).getBytes()),
                    "validator_config"));

    builder.addBodyPart(new StringPart("end_block", endBlockNumber.toString()));
    Request r = builder.build();
    final Response divideResponse = HttpClient.post(r);
    LogUtils.info(
        logger,
        () -> Log.newBuilder().kv("deadlinePeriod response", divideResponse.getResponseBody()));
    List<PeriodInfo> pairList;
    try {
      pairList =
          JSON.parseObject(
              divideResponse.getResponseBody(), new TypeReference<List<PeriodInfo>>() {});
    } catch (Exception e) {
      logger.error("period list parse error", e);
      throw new MtoolClientException(divideResponse.getResponseBody());
    }
    return pairList;
  }

  private static class RequestServiceHolder {

    private static final RequestService instance = new RequestService();
  }
}
