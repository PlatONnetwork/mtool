package com.platon.mtool.client;

import com.alaya.bech32.Bech32;
import com.alibaba.fastjson.JSON;
import com.alaya.parameters.NetworkParameters;
import com.alaya.crypto.Address;
// Bech32地址生成
public class Bech32GeneratorTest {
    public static void main(String[] args) {
        String hexAddress = "a1548dd61010a742cd66fb86324ab3e29355864a";
        if(!hexAddress.startsWith("0x")) hexAddress = "0x"+hexAddress;
        String testnetAddress = Bech32.addressEncode(NetworkParameters.TestNetParams.getHrp(),hexAddress);
        String mainnetAddress = Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(),hexAddress);
        Address address = new Address();
        address.setMainnet(mainnetAddress);
        address.setTestnet(testnetAddress);
        System.out.println(JSON.toJSONString(address,true));


        String origin = "atx1592gm4sszzn59ntxlwrryj4nu2f4tpj2jyfnyc";
        String hexStr = Bech32.addressDecodeHex(origin);
        String testAdd = Bech32.addressEncode(NetworkParameters.TestNetParams.getHrp(), hexStr);
        String mainAdd = Bech32.addressEncode(NetworkParameters.MainNetParams.getHrp(), hexStr);
        address = new Address();
        address.setMainnet(mainAdd);
        address.setTestnet(testAdd);
        System.out.println(JSON.toJSONString(address,true));
    }
}
