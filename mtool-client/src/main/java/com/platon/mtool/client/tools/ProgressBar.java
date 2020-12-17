package com.platon.mtool.client.tools;

import java.util.HashSet;
import java.util.Set;

/**
 * 点状进度条
 *
 * <p>Created by liyf.
 */
public class ProgressBar implements Runnable {

  private static Set<Thread> threadMap = new HashSet<>();

  public static void start() {
    Thread thread = new Thread(new ProgressBar());
    threadMap.add(thread);
    thread.start();
  }

  public static void stop() {
    stop("operation finished");
  }

  public static void stop(String msg) {
    for (Thread thread : threadMap) {
      thread.interrupt();
    }
    PrintUtils.echo("");
    PrintUtils.echo(msg);
  }

  @Override
  public void run() {
    try {
      // 防止无限循环
      int j = 0;
      do {
        j++;
        Thread.sleep(1000);
        System.out.print(".");
      } while (j != Integer.MIN_VALUE);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
