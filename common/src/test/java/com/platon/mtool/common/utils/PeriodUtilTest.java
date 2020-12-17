package com.platon.mtool.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.List;
import org.junit.jupiter.api.Test;

/** Created by liyf. */
class PeriodUtilTest {

  @Test
  void calcSettlePeriod() {
    Integer period10 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(10), 100);
    Integer period100 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(100), 100);
    Integer period101 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(101), 100);
    assertEquals(1, period10);
    assertEquals(1, period100);
    assertEquals(2, period101);
  }

  @Test
  void calcIncreasePeriod() {
    Integer period10 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(10), 100);
    Integer period100 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(100), 100);
    Integer period101 = PeriodUtil.calcIncreasePeriod(BigInteger.valueOf(101), 100);
    assertEquals(1, period10);
    assertEquals(1, period100);
    assertEquals(2, period101);
  }

  @Test
  void getLastBlockNum() {
    BigInteger lastBlockNumOfPeriod1 = PeriodUtil.getLastBlockNum(1, 1000);
    BigInteger lastBlockNumOfPeriod10 = PeriodUtil.getLastBlockNum(10, 1000);
    assertEquals(BigInteger.valueOf(1000), lastBlockNumOfPeriod1);
    assertEquals(BigInteger.valueOf(10000), lastBlockNumOfPeriod10);
  }

  @Test
  void getFirstBlockNum() {
    BigInteger firstBlockNumOfPeriod1 = PeriodUtil.getFirstBlockNum(1, 1000);
    BigInteger firstBlockNumOfPeriod10 = PeriodUtil.getFirstBlockNum(10, 1000);
    assertEquals(BigInteger.valueOf(1), firstBlockNumOfPeriod1);
    assertEquals(BigInteger.valueOf(9001), firstBlockNumOfPeriod10);
  }

  @Test
  void getPeriodList() {
    List<Integer> list =
        PeriodUtil.getPeriodList(BigInteger.valueOf(241), BigInteger.valueOf(2400), 240);
    assertEquals(9, list.size());
    assertEquals("[2, 3, 4, 5, 6, 7, 8, 9, 10]", list.toString());
  }
}
