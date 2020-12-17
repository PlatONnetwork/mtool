package com.platon.mtool.client.options;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.platon.mtool.client.CommonOption;
import com.platon.mtool.client.converter.KeystoreConverter;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllParams;
import com.platon.mtool.common.web3j.Keystore;

/** Created by liyf. */
@Parameters(
    commandNames = {AllCommands.CREATE_OBSERVEWALLET},
    commandDescriptionKey = AllCommands.CREATE_OBSERVEWALLET)
public class CreateObserveWalletOption extends CommonOption {

  @Parameter(
      names = {AllParams.KEYSTORE},
      descriptionKey = AllParams.KEYSTORE,
      required = true,
      arity = 1,
      converter = KeystoreConverter.class)
  private Keystore keystore;

  public Keystore getKeystore() {
    return keystore;
  }

  public void setKeystore(Keystore keystore) {
    this.keystore = keystore;
  }
}
