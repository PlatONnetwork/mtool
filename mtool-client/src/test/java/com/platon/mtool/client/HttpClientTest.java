package com.platon.mtool.client;

import static org.asynchttpclient.Dsl.post;

import com.platon.mtool.client.httpclients.HttpJunitServer;
import com.platon.mtool.client.tools.HttpClient;
import org.asynchttpclient.Request;
import org.asynchttpclient.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class HttpClientTest {

  private static int serverPort = 7788;

  @BeforeAll
  static void before() throws Exception {
    HttpJunitServer.start(serverPort);
  }

  @AfterAll
  static void after() {
    HttpJunitServer.shutdown();
  }

  @Test
  void test() {
    Request r = post("http://localhost:" + serverPort + "/hello").build();
    Response res = HttpClient.post(r);
    Assertions.assertEquals("hello word", res.getResponseBody());
  }
}
