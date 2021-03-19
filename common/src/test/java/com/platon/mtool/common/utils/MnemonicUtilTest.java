package com.platon.mtool.common.utils;

import com.platon.crypto.ECKeyPair;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MnemonicUtilTest {
    String demoMnemonic = "sponsor sad lottery summer dose thunder harvest town beauty joy empty eyebrow";
    String demoPubKey = "4024873823472660321724841583513172955438703126521392306038623128161445759446642767270305470335158799812424932447274866141391259859397193430067276169503561";
    @Test
    void testGenerateMnemonic() throws IOException {
        String  mnemonic = MnemonicUtil.generateMnemonic();
        System.out.println(mnemonic);
    }

    @Test
    void testGenerateECKeyPair() throws IOException {
        String mnemonic = MnemonicUtil.generateMnemonic();
        ECKeyPair keyPair = MnemonicUtil.generateECKeyPair(demoMnemonic);
        assertEquals(demoPubKey, keyPair.getPublicKey().toString());

    }
}
