package com.platon.mtool.common.utils;

import com.platon.crypto.ECKeyPair;
import com.platon.crypto.MnemonicUtils;
import com.platon.mtool.common.exception.MtoolClientException;
import org.bitcoinj.crypto.*;

import java.security.SecureRandom;
import java.util.List;

/**
 * 助记词工具
 *
 * <p>Created by liyf
 */
public class MnemonicUtil {
  static final String PATH = "M/44H/206H/0H/0";

  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  public static String generateMnemonic() {
    byte[] initialEntropy = new byte[16];
    SECURE_RANDOM.nextBytes(initialEntropy);
    return MnemonicUtils.generateMnemonic(initialEntropy);
  }

  public static ECKeyPair generateECKeyPair(String mnemonic) {
    if (!MnemonicUtils.validateMnemonic(mnemonic)) {
      throw new MtoolClientException("Mnemonic phrase cannot be recognized");
    }
    // 2.生成种子
    byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
    // 3. 生成根Keystore root private key 树顶点的master key ；bip32
    DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seed);
    // 4. 由根Keystore生成 第一个HD 钱包
    DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);
    // 5. 定义父路径 H则是加强
    List<ChildNumber> parentPath = HDUtils.parsePath(PATH);
    // 6. 由父路径,派生出第一个子Keystore "new ChildNumber(0)" 表示第一个（PATH）
    DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
    // 7.通过Keystore生成公Keystore对
    return ECKeyPair.create(child.getPrivKeyBytes());
  }
}
