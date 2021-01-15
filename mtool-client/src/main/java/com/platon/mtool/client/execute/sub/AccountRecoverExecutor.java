package com.platon.mtool.client.execute.sub;

import com.beust.jcommander.JCommander;
import com.platon.crypto.Credentials;
import com.platon.crypto.ECKeyPair;
import com.platon.crypto.WalletUtils;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.AccountOptions.RecoverOption;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.AllCommands.Account;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.exception.MtoolClientExceptionCode;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.utils.MnemonicUtil;
import com.platon.mtool.common.web3j.Keystore.Type;
import com.platon.utils.Numeric;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 恢复钱包
 *
 * <p>Created by liyf
 */
public class AccountRecoverExecutor extends MtoolExecutor<RecoverOption> {
  private static final Logger logger = LoggerFactory.getLogger(AccountRecoverExecutor.class);

  private BlockChainService blockChainService = BlockChainService.singleton();

  public AccountRecoverExecutor(JCommander commander, RecoverOption commonOption) {
    super(commander, commonOption);
  }

  @Override
  public void execute(RecoverOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg(Account.RECOVER).kv("option", option));
    String nameRegex = ".{1,12}";
    String name = option.getName();

    if (name != null && !name.matches(nameRegex)) {
      throw new MtoolClientException("Incorrect name format");
    }

    if (StringUtils.isEmpty(name)) {
      name = getAccountName();
    }

    Path dir = ResourceUtils.getKeystorePath().toAbsolutePath();
    String filename = name + ".json";

    if (dir.resolve(filename).toFile().exists()) {
      throw new MtoolClientException("Existed wallet name");
    }

    if (!option.isPrivateKey() && !option.isMnemonics()) {
      help();
      return;
    }

    char[] pass = PrintUtils.readPassword("Enter a passphrase to encrypt your key to disk: ");
    if (pass.length == 0) {
      throw new MtoolClientException("Password required");
    }
    if (pass.length < 6) {
      throw new MtoolClientException("6 characters at least");
    }
    String passStr = new String(pass);

    String pswRegex = "^\\S{6,}$";
    if (!passStr.matches(pswRegex)) {
      throw new MtoolClientException("Incorrect password format");
    }

    char[] confirmPass = PrintUtils.readPassword("Repeat the passphrase: ");
    if (!Arrays.equals(pass, confirmPass)) {
      throw new MtoolClientException("Password mismatch");
    }

    ECKeyPair ecKeyPair;
    String originName;
    if (option.isPrivateKey()) {
      String privateKey = PrintUtils.interact("Enter your 64bit Private Key: ");
      blockChainService.validPrivateKey(privateKey);

      ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
      originName = WalletUtils.generatePlatONWalletFile(passStr, ecKeyPair, dir.toFile());
    } else if (option.isMnemonics()) {
      String mnemonics = PrintUtils.interact("Enter your bip39 mnemonic: ");
      if (StringUtils.isEmpty(mnemonics)) {
        throw new MtoolClientException("Mnemonic phrase can't be empty");
      }
      ecKeyPair = MnemonicUtil.generateECKeyPair(mnemonics);
      originName = WalletUtils.generatePlatONWalletFile(passStr, ecKeyPair, dir.toFile());
    } else {
      throw MtoolClientExceptionCode.COMMAND_NOT_FOUND.create();
    }

    LogUtils.info(logger, () -> Log.newBuilder().msg(originName));
    File originFile = new File(dir.resolve(originName).toAbsolutePath().toString());
    File destFile = new File(dir.resolve(filename).toAbsolutePath().toString());
    boolean ret = originFile.renameTo(destFile);
    if (ret) {
      LogUtils.info(logger, () -> Log.newBuilder().msg("recover wallet success"));
      Credentials credentials = WalletUtils.loadCredentials(passStr, destFile);
      PrintUtils.echo("-name: %s", name);
      PrintUtils.echo("-type: %s", Type.NORMAL.name());
      PrintUtils.echo("-address: %s", credentials.getAddress());
      PrintUtils.echo("-public key: 0x%s", credentials.getEcKeyPair().getPublicKey().toString(16));
    }
  }

  protected String getAccountName() throws IOException {

    Path rewardPath = ResourceUtils.getKeystorePath();
    String prefix = "Account";
    int num = 0;
    try (DirectoryStream<Path> stream =
        Files.newDirectoryStream(rewardPath, prefix + "{,[0-9]*}")) {

      for (Path entry : stream) {
        Pattern pattern = Pattern.compile("(\\d+)\\.json$");
        Matcher matcher = pattern.matcher(entry.toString());
        if (matcher.find()) {
          int tmp = Integer.parseInt(matcher.group(1));
          num = Math.max(num, tmp);
        }
      }
    }
    return prefix + (num + 1);
  }
}
