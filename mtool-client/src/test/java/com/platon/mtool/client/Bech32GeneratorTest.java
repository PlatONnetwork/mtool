package com.platon.mtool.client;

import com.platon.bech32.Bech32;
import com.platon.parameters.NetworkParameters;
// Bech32地址生成
public class Bech32GeneratorTest {
    public static void main(String[] args) {
        String hexAddress = "a1548dd61010a742cd66fb86324ab3e29355864a";
        if(!hexAddress.startsWith("0x")) hexAddress = "0x"+hexAddress;
        String bech32Address = Bech32.addressEncode(NetworkParameters.getHrp(),hexAddress);
        System.out.println("bech32Address:" + bech32Address);

        String origin = "atx1592gm4sszzn59ntxlwrryj4nu2f4tpj2jyfnyc";
        String hexStr = Bech32.addressDecodeHex(origin);
        String destAddr = Bech32.addressEncode(NetworkParameters.getHrp(), hexStr);
        System.out.println("origin:" + origin + "  ;  destAddr:" + destAddr);
    }
}
