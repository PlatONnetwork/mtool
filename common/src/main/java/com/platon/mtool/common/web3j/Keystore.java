package com.platon.mtool.common.web3j;

import com.alibaba.fastjson.JSON;
import com.platon.crypto.Credentials;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 钱包model
 *
 * <p>Created by liyf.
 */
public class Keystore {
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

    private static final String MAIN_TEST_ADDRESS_REGEX = "\\\"address\\\"\\s*:\\s*\\{\\s*\\\"mainnet\\\"\\s*:\\s*\\\"(\\S*)\\\"[^}]*\\}";
    public static Keystore loadKeystore(String filePath){
        Keystore keystore = null;

        File file = new File(filePath);
        String fileContent = null;
        try {
            fileContent = com.platon.utils.Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(fileContent)){
            return null;
        }
        try {
            fileContent = fileContent.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");
            keystore = JSON.parseObject(fileContent, Keystore.class);
        }catch ( Throwable t){
            throw new RuntimeException("Invalid wallet observe keystore file", t);
        }
        return keystore;
    }
}
