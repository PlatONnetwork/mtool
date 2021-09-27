package com.platon.mtool.common.web3j;

import com.alibaba.fastjson.JSON;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletFile;
import com.platon.crypto.WalletUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 钱包model
 *
 * <p>Created by liyf.
 */
public class Keystore {
    private static final Logger logger = LoggerFactory.getLogger(Keystore.class);
    private Type type;
    private String address;           //type = OBSERVE时，有用; type = NORMAL时，就是 credentials.getAddress()
    private Credentials credentials;  //type = NORMAL时，有用
    private String filepath;

    public String getAddress() {
        if (type == Type.NORMAL) {
            return credentials.getAddress();
        } else {
            return address;
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public enum Type {
        // 普通钱包， 观察钱包
        NORMAL,
        OBSERVE
    }

    private static final String MAIN_TEST_ADDRESS_REGEX = "\\\"address\\\"\\s*:\\s*\\{\\s*\\\"mainnet\\\"\\s*:\\s*\\\"([A-Za-z0-9]+)\\\"[^}]*\\}";
    public static Keystore loadKeystore(String filePath){
        Keystore keystore = null;


        File file = new File(filePath);
        String fileContent = null;
        try {
            fileContent = com.platon.utils.Files.readString(file);
        } catch (IOException e) {
            logger.error("",e);
        }
        if (StringUtils.isBlank(fileContent)){
            return null;
        }
        try {
            fileContent = fileContent.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
            keystore = JSON.parseObject(fileContent, Keystore.class);
            keystore.setFilepath(file.getAbsolutePath());
        }catch ( Throwable t){
            throw new RuntimeException("Invalid wallet observe keystore file", t);
        }
        return keystore;
    }
//
//    public static void main(String args[]) throws IOException {
//       //System.out.println(MAIN_TEST_ADDRESS_REGEX);
//
//        String json = "{\n" +
//                "\t\"address\": {\n" +
//                "\t\t\"mainnet\": \"atp1wcdlkaa232h2yyyly84r70skzuc5q5f98rn3v5\",\n" +
//                "\t\t\"testnet\": \"atx1wcdlkaa232h2yyyly84r70skzuc5q5f9d90ml7\"\n" +
//                "\t},\n" +
//                "\t\"id\": \"78b57765-8bfd-47cd-8b1d-e59c2bebb4ac\",\n" +
//                "\t\"version\": 3,\n" +
//                "\t\"crypto\": {\n" +
//                "\t\t\"cipher\": \"aes-128-ctr\",\n" +
//                "\t\t\"cipherparams\": {\n" +
//                "\t\t\t\"iv\": \"69156eed9425ec092f4352be9a39bf14\"\n" +
//                "\t\t},\n" +
//                "\t\t\"ciphertext\": \"e879d6bc2f5fa4357111994a4a32159e0cf1e860693852a45c57d8916f2da0ec\",\n" +
//                "\t\t\"kdf\": \"scrypt\",\n" +
//                "\t\t\"kdfparams\": {\n" +
//                "\t\t\t\"dklen\": 32,\n" +
//                "\t\t\t\"n\": 16384,\n" +
//                "\t\t\t\"p\": 1,\n" +
//                "\t\t\t\"r\": 8,\n" +
//                "\t\t\t\"salt\": \"2001307219e0099790806f7b54e6afff3457302e2fefc3e21548854597b7bcf8\"\n" +
//                "\t\t},\n" +
//                "\t\t\"mac\": \"0f2f925ff4ce9464fbbacc86524762e444278ad6b94e740921f8f1a8a2d0bb50\"\n" +
//                "\t}\n" +
//                "}\n";
//        json = json.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
//        System.out.println(json);
//
//        json = "{\"address\":{\"mainnet\":\"atp1wcdlkaa232h2yyyly84r70skzuc5q5f98rn3v5\",\"testnet\":\"atx1wcdlkaa232h2yyyly84r70skzuc5q5f9d90ml7\"},\"id\":\"78b57765-8bfd-47cd-8b1d-e59c2bebb4ac\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"69156eed9425ec092f4352be9a39bf14\"},\"ciphertext\":\"e879d6bc2f5fa4357111994a4a32159e0cf1e860693852a45c57d8916f2da0ec\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":16384,\"p\":1,\"r\":8,\"salt\":\"2001307219e0099790806f7b54e6afff3457302e2fefc3e21548854597b7bcf8\"},\"mac\":\"0f2f925ff4ce9464fbbacc86524762e444278ad6b94e740921f8f1a8a2d0bb50\"}}";
//        json = json.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
//        System.out.println(json);
//
//
//        String jsonPath = "D:\\javalang\\github.com\\mtool\\mtool-client\\src\\test\\resources\\staking_observed.json";
//        File jsonFile = new File(jsonPath);
//        WalletFile walletFile = WalletUtils.loadWalletFile(jsonFile);
//        System.out.println("WalletFile Address:"+walletFile.getAddress());
//
//        Keystore keystore = loadKeystore(jsonPath);
//        System.out.println("keystore Address:"+keystore.getAddress());
//    }
}
