package com.platon.mtool.client.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BaseConverter;
import com.platon.crypto.CipherException;
import com.platon.crypto.Credentials;
import com.platon.crypto.WalletUtils;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.web3j.Keystore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 钱包转换器
 *
 * <p>Created by liyf.
 */
public class KeystoreConverter extends BaseConverter<Keystore> {

    private static final Logger logger = LoggerFactory.getLogger(KeystoreConverter.class);
    private PathConverter pathConverter = new PathConverter(getOptionName());

    public KeystoreConverter(String optionName) {
        super(optionName);
    }

    @Override
    public Keystore convert(String value) {
        Path path = pathConverter.convert(value);
        String pathStr = path.toAbsolutePath().toString();
        Keystore keystore = new Keystore();
        if (getOptionName().equals(AllParams.ADDRESS)) {
            return convertObserve(path);
        } else if (getOptionName().equals(AllParams.KEYSTORE)) {
            char[] pass = PrintUtils.readPassword("please input keystore password: ");
            return convertKeystore(pathStr, new String(pass));
        }
        return keystore;
    }

    private static final String MAIN_TEST_ADDRESS_REGEX = "\\\"address\\\"\\s*:\\s*\\{\\s*\\\"mainnet\\\"\\s*:\\s*\\\"(\\S*)\\\"[^}]*\\}";

    public Keystore convertObserve(Path path) {
        Keystore keystore = null;

        String filePath = path.toAbsolutePath().toString();
        File file = new File(filePath);


        String fileContent = null;
        try {
            fileContent = com.platon.utils.Files.readString(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileContent = fileContent.replaceAll(MAIN_TEST_ADDRESS_REGEX, "\"address\": \"$1\"");

            keystore = JSON.parseObject(fileContent, Keystore.class);

            if (keystore != null) {
                keystore.setFilepath(filePath);
                if (!Keystore.Type.OBSERVE.equals(keystore.getType())) {
                    throw new ParameterException("Keystore type is not the expected OBSERVE.");
                }
            }
        }catch ( Throwable t){
            throw new ParameterException("Invalid wallet observe keystore file", t);
        }
        return keystore;


    }

    public Keystore convertObserve_old(Path path) {
        String pathStr = path.toAbsolutePath().toString();
        Keystore keystore = new Keystore();
        try (InputStream is = Files.newInputStream(path)) {
            keystore = JSON.parseObject(is, Keystore.class);
        } catch (IOException | JSONException e) {
            throw new ParameterException(
                    getErrorString(pathStr, "Invalid wallet observe keystore file"));
        }
        keystore.setFilepath(pathStr);
        if (!Keystore.Type.OBSERVE.equals(keystore.getType())) {
            throw new ParameterException("address is not an observe keystore");
        }
        return keystore;
    }

    public Keystore convertKeystore(String pathStr, String password) {
        Keystore keystore = new Keystore();
        Credentials credentials;
        try {
            credentials = WalletUtils.loadCredentials(password, pathStr);
        } catch (IOException e) {
            logger.error("Invalid wallet keystore file", e);
            throw new ParameterException(getErrorString(pathStr, "Invalid wallet keystore file"));
        } catch (CipherException e) {
            logger.error("Incorrect password", e);
            throw new ParameterException(getErrorString(pathStr, "Incorrect password"));
        }
        keystore.setType(Keystore.Type.NORMAL);
        keystore.setCredentials(credentials);
        keystore.setFilepath(pathStr);
        return keystore;
    }

    @Override
    protected String getErrorString(String value, String to) {
        return "\"" + getOptionName() + "\": " + value + " (" + to + " )";
    }
}
