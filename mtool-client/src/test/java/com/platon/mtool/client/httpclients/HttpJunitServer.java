package com.platon.mtool.client.httpclients;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Created by liyf. */
public final class HttpJunitServer {

  private static final Logger logger = LoggerFactory.getLogger(HttpJunitServer.class);

  private static EventLoopGroup bossGroup;
  private static EventLoopGroup workerGroup;

  public static void start(int port) throws Exception {
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();
    ServerBootstrap b = new ServerBootstrap();
    b.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new HttpJunitServerInitializer());

    b.bind(port).sync().channel();
    logger.info("start junit test server " + ("http") + "://localhost:" + port + '/');
  }

  public static void shutdown() {
    Future<?> bossFuture = bossGroup.shutdownGracefully();
    Future<?> workerFuture = workerGroup.shutdownGracefully();
    try {
      bossFuture.await();
      workerFuture.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
