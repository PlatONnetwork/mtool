package com.platon.mtool.common.utils;

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
    assertEquals("0xa1548dd61010a742cd66fb86324ab3e29355864a", AddressUtil.getAddressByNodeId(nodeId));
  }

 }
