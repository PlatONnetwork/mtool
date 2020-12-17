package com.platon.mtool.common.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 周期工具
 *
 * <p>Created by liyf.
 */
public class PeriodUtil {

  private PeriodUtil() {}

  /**
   * 计算传入区块属于第几个结算周期.
   *
   * @param blockNum 当前区块号
   * @param settlePeriodSize 结算周期块数
   * @return 当前所属结算周期
   */
  public static Integer calcSettlePeriod(BigInteger blockNum, Integer settlePeriodSize) {
    if (blockNum.equals(BigInteger.ZERO)) {
      return 0;
    }
    return blockNum
            .subtract(BigInteger.ONE)
            .divide(new BigInteger(settlePeriodSize.toString()))
            .intValue()
        + 1;
  }

  /**
   * 计算传入区块属于第几个增发周期.
   *
   * @param blockNum 当前区块号
   * @param increasePeriodSize 增发周期块数
   * @return 当前所属增发周期
   */
  public static Integer calcIncreasePeriod(BigInteger blockNum, Integer increasePeriodSize) {
    return calcSettlePeriod(blockNum, increasePeriodSize);
  }

  /**
   * 获取当前周期的最后一个区块.
   *
   * @param period 结算周期序号
   * @param periodSize 结算周期大小
   * @return 结算周期最后一个区块
   */
  public static BigInteger getLastBlockNum(Integer period, Integer periodSize) {
    return BigInteger.valueOf(period).multiply(BigInteger.valueOf(periodSize));
  }

  /**
   * 获取当前周期的第一个区块.
   *
   * @param period 结算周期序号
   * @param periodSize 结算周期大小
   * @return 结算周期第一个区块
   */
  public static BigInteger getFirstBlockNum(Integer period, Integer periodSize) {
    return BigInteger.valueOf(periodSize)
        .multiply(BigInteger.valueOf(period - 1L))
        .add(BigInteger.ONE);
  }

  public static List<Integer> getPeriodList(
      BigInteger startBlockNum, BigInteger endBlockNum, Integer periodSize) {
    List<Integer> list = new ArrayList<>();
    Integer firstPeriod = calcSettlePeriod(startBlockNum, periodSize);
    Integer endPeriod = calcSettlePeriod(endBlockNum, periodSize);
    // 去掉0周期
    firstPeriod = firstPeriod == 0 ? 1 : firstPeriod;
    for (int i = firstPeriod; i <= endPeriod; i++) {
      list.add(i);
    }
    return list;
  }
}
