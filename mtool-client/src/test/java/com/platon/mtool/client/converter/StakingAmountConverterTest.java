package com.platon.mtool.client.converter;

import com.beust.jcommander.ParameterException;
import com.platon.contracts.ppos.dto.enums.StakingAmountType;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.entity.StakingAmount;
import com.platon.mtool.common.utils.PlatOnUnit;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Created by liyf. */
class StakingAmountConverterTest {

  private StakingAmountConverter amountCconverter = new StakingAmountConverter(AllParams.AMOUNT);
  private StakingAmountConverter restrictedAmountConverter =
      new StakingAmountConverter(AllParams.RESTRICTEDAMOUNT);
  private StakingAmountConverter autoConverter = new StakingAmountConverter(AllParams.AUTO_AMOUNT);
  @Test
  void convert() {
    StakingAmount amount = amountCconverter.convert("1");
    assertEquals(PlatOnUnit.latToVon(BigInteger.ONE), amount.getAmount());
    assertEquals(StakingAmountType.FREE_AMOUNT_TYPE, amount.getAmountType());
  }

  @Test
  void autoConvert() {
    StakingAmount amount = autoConverter.convert("1");
    assertEquals(PlatOnUnit.latToVon(BigInteger.ONE), amount.getAmount());
    assertEquals(StakingAmountType.AUTO_AMOUNT_TYPE, amount.getAmountType());
  }

  @Test
  void convertRestric() {
    StakingAmount amount = restrictedAmountConverter.convert("1");
    assertEquals(PlatOnUnit.latToVon(BigInteger.ONE), amount.getAmount());
    assertEquals(StakingAmountType.RESTRICTING_AMOUNT_TYPE, amount.getAmountType());
  }

  @Test
  void convertException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> amountCconverter.convert("1a"));
    assertEquals("Invalid amount", exception.getMessage());
  }

  @Test
  void convertSizeException() {
    ParameterException exception =
        assertThrows(ParameterException.class, () -> amountCconverter.convert(".123456789"));
    assertEquals("amount cannot exceed 8 decimal places", exception.getMessage());
  }

  @Test
  public void test03() {

    Instant start = Instant.now();
    LongStream.rangeClosed( 0,110 )
            //并行流
            .parallel()
            .reduce( 0,Long::sum );




    LongStream.rangeClosed( 0,110 )
            //顺序流
            .sequential()
            .reduce( 0,Long::sum );


    Instant end = Instant.now();
    System.out.println("耗费时间"+ Duration.between( start,end ).toMillis());

  }

}
