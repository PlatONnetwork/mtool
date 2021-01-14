package com.platon.mtool.common.entity;

import com.platon.crypto.WalletFile;
import com.platon.mtool.common.web3j.Keystore;

public class ObservedWalletFile extends WalletFile {
    private Keystore.Type type;

    public Keystore.Type getType() {
        return type;
    }

    public void setType(Keystore.Type type) {
        this.type = type;
    }
}
