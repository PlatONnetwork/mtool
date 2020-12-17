package com.platon.mtool.common.utils;

import com.alaya.bech32.Bech32;
import com.alaya.crypto.Credentials;
import com.alaya.parameters.NetworkParameters;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Created by liyf. */
class AddressUtilTest {

  @Test
  void isValid() {
    assertFalse(AddressUtil.isValid(null));
    assertTrue(AddressUtil.isValid("0xa1548dd61010a742cd66fb86324ab3e29355864a"));
    assertFalse(AddressUtil.isValid("a1548dd61010a742cd66fb86324ab3e29355864a"));
  }

  @Test
  void format() {
    assertEquals("", AddressUtil.format(""));
    assertEquals(
        "0xa1548dd61010a742cd66fb86324ab3e29355864a",
        AddressUtil.format("0xa1548dd61010a742cd66fb86324ab3e29355864a"));
    assertEquals(
        "0xa1548dd61010a742cd66fb86324ab3e29355864a",
        AddressUtil.format("a1548dd61010a742cd66fb86324ab3e29355864a"));
  }

  @Test
  public void shouldEquals() {
    String nodeId =
        "0x439d6fc6dec066d1666cb23fa323fdb98f4dc97f7d2c486fced988d4aa87"
            + "d9a50f6e52e984ea3a4c8783f8a77110d351d321c841996f6145afe929aa99df9f6e";
    assertEquals("0xa1548dd61010a742cd66fb86324ab3e29355864a", AddressUtil.getAddress(nodeId));
  }

  @Test
  void getTargetChainAccountAddress() {
    assertEquals("atp1rsd7azkcdex95slmprgs509pcyv0a0affvjdu9", AddressUtil.getTargetChainAccountAddress(NetworkParameters.MainNetParams.getChainId(),"atx1rsd7azkcdex95slmprgs509pcyv0a0afr2w800"));
  }

  @Test
  void isValidTargetChainAccountAddress() {
    assertFalse( AddressUtil.isValidTargetChainAccountAddress(NetworkParameters.MainNetParams.getChainId(),"atx1rsd7azkcdex95slmprgs509pcyv0a0afr2w800"));
  }


  @Test
  void getAddress() {
    System.out.println(NetworkParameters.TestNetParams.getChainId());
    System.out.println(NetworkParameters.MainNetParams.getChainId());

    System.out.println(Bech32.addressEncode(NetworkParameters.TestNetParams.getHrp(), Bech32.addressDecodeHex("atp1uqx03pam9uv6dr0pjv3tmvqml7n0sg6svhuph3")));

    System.out.println(Credentials.create("5442e642898958c5d85c6dd93adf4c9f7fe606134c26394c1dfc5fae60338fe4").getAddress(201030));

    System.out.println(Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(),Bech32.addressDecodeHex("lax12jn6835z96ez93flwezrwu4xpv8e4zatc4kfru")));
    System.out.println(Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(),Bech32.addressDecodeHex("lax196278ns22j23awdfj9f2d4vz0pedld8au6xelj")));
    System.out.println(Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(),Bech32.addressDecodeHex("lax1zqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqrzpqayr")));
  }

}
