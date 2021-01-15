package com.platon.mtool.client.execute;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.ClientConsts;
import com.platon.mtool.client.options.CreateObserveWalletOption;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.web3j.Keystore;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;

/**
 * 创建观察钱包
 *
 * <p>Created by liyf.
 */
public class CreateObserveWalletExecutor extends MtoolExecutor<CreateObserveWalletOption> {

  private static final Logger logger = LoggerFactory.getLogger(CreateObserveWalletExecutor.class);

  public CreateObserveWalletExecutor(JCommander commander, CreateObserveWalletOption commonOption) {
    super(commander, commonOption);
  }

  @Override
  public void execute(CreateObserveWalletOption option) throws Exception {
    LogUtils.info(logger, () -> Log.newBuilder().msg("CreateObserveWallet").kv("option", option));
    String originName = FilenameUtils.getBaseName(option.getKeystore().getFilepath());
    String observeName = originName + "_observed.json";
    Keystore keystore = new Keystore();
    keystore.setType(Keystore.Type.OBSERVE);
    String absolutePath =
        ResourceUtils.getKeystorePath().resolve(observeName).toAbsolutePath().toString();
    try (FileOutputStream fos = new FileOutputStream(absolutePath)) {
      fos.write(JSON.toJSONString(keystore).getBytes());
      fos.flush();
    }
    PrintUtils.echo(ClientConsts.SUCCESS);
    PrintUtils.echo("wallet created at: %s", absolutePath);
  }
}
