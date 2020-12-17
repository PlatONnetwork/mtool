package com.platon.mtool.client.httpclients;

import static com.platon.mtool.client.ClientConsts.URL_CLAWLER_TO_DEADLINE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_DIVIDE_RESULT;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_GENFILE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_GENTOTALFILE;
import static com.platon.mtool.client.ClientConsts.URL_REWARD_VALID_DIVIDE;
import static com.platon.mtool.client.ClientConsts.URL_SYSTEM_PREPARE_WEB3J;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_DISPOSITION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.platon.mtool.client.ClientConsts;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

/** Created by liyf. */
public class HttpJunitServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request)
      throws FileNotFoundException {
    FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
    response.headers().set(CONTENT_TYPE, "application/json; charset=UTF-8");
    if (request.uri().contains("/hello")) {
      response.content().writeBytes("hello word".getBytes());
    } else if (request.uri().contains(ClientConsts.URL_REWARD_VALID_BLOCK_NUMBER)) {
      // language=JSON
      response
          .content()
          .writeBytes(
              "{\"startBlockNumber\": 1201, \"endBlockNumber\": 1500}"
                  .getBytes());
    }else if (request.uri().contains(ClientConsts.URL_REWARD_DIVIDEPERIOD)) {
      // language=JSON
      response
          .content()
          .writeBytes(
              "[{\"startBlockNumber\": 1201, \"endBlockNumber\": 1500,\"settlePeriod\": 5}]"
                  .getBytes());
    } else if (request.uri().contains(ClientConsts.URL_CLAWLER_DEADLINE_PERIOD)) {
      // language=JSON
      response
          .content()
          .writeBytes(
              "[{\"startBlockNumber\": 1201, \"endBlockNumber\": 1500,\"settlePeriod\": 5}]"
                  .getBytes());
    } else if (request.uri().contains(URL_SYSTEM_PREPARE_WEB3J)) {
      response.content().writeBytes(ClientConsts.SUCCESS.getBytes());
    } else if (request.uri().contains(URL_REWARD_GENFILE)) {
      response
          .headers()
          .set(CONTENT_DISPOSITION, "attachment; filename=delegate_reward_expect.csv");
      String expect =
          new BufferedReader(
                  new InputStreamReader(
                      Objects.requireNonNull(
                          ClassLoader.getSystemResourceAsStream(
                              "csvfile/delegate_reward_expect.csv"))))
              .lines()
              .collect(Collectors.joining("\n"))
              .trim();
      response.content().writeBytes(expect.getBytes());
    } else if (request.uri().contains(URL_REWARD_GENTOTALFILE)) {
      response
          .headers()
          .set(CONTENT_DISPOSITION, "attachment; filename=delegate_reward_summary_expect.csv");
      String expect =
          new BufferedReader(
                  new InputStreamReader(
                      Objects.requireNonNull(
                          ClassLoader.getSystemResourceAsStream(
                              "csvfile/delegate_reward_summary_expect.csv"))))
              .lines()
              .collect(Collectors.joining("\n"))
              .trim();
      response.content().writeBytes(expect.getBytes());
    } else if (request.uri().contains(URL_REWARD_DIVIDE_RESULT)) {
      response.content().writeBytes(ClientConsts.SUCCESS.getBytes());
    } else if (request.uri().contains(URL_REWARD_VALID_DIVIDE)) {
      response.content().writeBytes(ClientConsts.SUCCESS.getBytes());
    } else if (request.uri().contains(URL_CLAWLER_TO_DEADLINE)) {
      response.content().writeBytes(ClientConsts.SUCCESS.getBytes());
    }
    ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
  }
}
