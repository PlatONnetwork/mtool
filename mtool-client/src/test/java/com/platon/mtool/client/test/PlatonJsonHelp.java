package com.platon.mtool.client.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import com.alaya.protocol.core.methods.response.PlatonBlock;
import com.alaya.protocol.core.methods.response.PlatonCall;
import com.alaya.protocol.core.methods.response.PlatonGetTransactionReceipt;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.protocol.core.methods.response.TransactionReceipt;
import com.alaya.utils.Numeric;

/** Created by liyf. */
public abstract class PlatonJsonHelp {

  public static PlatonBlock parseBlock(String jsonStr) {
    JSONObject jsonObject = JSON.parseObject(jsonStr);
    PlatonBlock.Block block = JSON.parseObject(jsonStr, PlatonBlock.Block.class);
    block.setNumber(Numeric.encodeQuantity(new BigInteger(jsonObject.get("number").toString())));
    block.setGasUsed(Numeric.encodeQuantity(new BigInteger(jsonObject.get("gasUsed").toString())));
    block.setGasLimit(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("gasLimit").toString())));
    block.setTimestamp(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("timestamp").toString())));

    JSONArray transactionJsonArray = jsonObject.getJSONArray("transactions");
    ParserConfig config = ParserConfig.getGlobalInstance();
    List<PlatonBlock.TransactionResult> transactionResultList = new ArrayList<>();
    for (Object o : transactionJsonArray) {
      JSONObject transactionJsonObject = (JSONObject) o;
      PlatonBlock.TransactionObject classItem =
          TypeUtils.cast(o, PlatonBlock.TransactionObject.class, config);
      classItem.setBlockNumber(
          Numeric.encodeQuantity(
              new BigInteger(transactionJsonObject.get("blockNumber").toString())));
      classItem.setGas(
          Numeric.encodeQuantity(new BigInteger(transactionJsonObject.get("gas").toString())));
      classItem.setGasPrice(
          Numeric.encodeQuantity(new BigInteger(transactionJsonObject.get("gasPrice").toString())));
      classItem.setValue(
          Numeric.encodeQuantity(
              new BigDecimal(transactionJsonObject.get("value").toString()).toBigInteger()));
      transactionResultList.add(classItem);
    }
    block.setTransactions(transactionResultList);
    PlatonBlock platonBlock = new PlatonBlock();
    platonBlock.setResult(block);
    return platonBlock;
  }

  public static PlatonSendTransaction parseTransaction(String jsonStr) {
    JSONObject jsonObject = JSON.parseObject(jsonStr);
    PlatonSendTransaction transaction = new PlatonSendTransaction();
    transaction.setResult(jsonObject.get("hash").toString());
    return transaction;
  }

  public static PlatonGetTransactionReceipt parseReceipt(String jsonStr) {
    JSONObject jsonObject = JSON.parseObject(jsonStr);
    TransactionReceipt receipt = JSON.parseObject(jsonStr, TransactionReceipt.class);
    receipt.setBlockNumber(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("blockNumber").toString())));
    receipt.setGasUsed(
        Numeric.encodeQuantity(new BigInteger(jsonObject.get("gasUsed").toString())));
    PlatonGetTransactionReceipt platonGetTransactionReceipt = new PlatonGetTransactionReceipt();
    platonGetTransactionReceipt.setResult(receipt);
    return platonGetTransactionReceipt;
  }

  public static PlatonCall parseVerifierList() {
    PlatonCall platonCall = new PlatonCall();
    platonCall.setResult(
        "0x7b22436f6465223a302c2244617461223a225b7b5c224e6f646549645c223a5c2234333964366663366465633036366431363636636232336661333233666462393866346463393766376432633438366663656439383864346161383764396135306636653532653938346561336134633837383366386137373131306433353164333231633834313939366636313435616665393239616139396466396636655c222c5c22426c735075624b65795c223a5c226139376538333563386633303462393163396565626232326537653834363134633133366433373339646435666330376136666134323538333838636566393138613832643139333763646662633738303666333466663032326533613730333261643130373038306430616161323266626563666464336236613263303863333365653737366137336364326564373364613162653036623366366535316362323732663037373261666430383833383137633165396330363464353739345c222c5c225374616b696e67416464726573735c223a5c223078613135343864643631303130613734326364363666623836333234616233653239333535383634615c222c5c2242656e65666974416464726573735c223a5c223078653362326639366530633235366264386332343361333231633864616561313235643761613439655c222c5c225374616b696e675478496e6465785c223a302c5c2250726f6772616d56657273696f6e5c223a313739322c5c225374616b696e67426c6f636b4e756d5c223a3334372c5c225368617265735c223a5c2230783432336439393766613635383137653230303030305c222c5c2245787465726e616c49645c223a5c226c6979667465737469645c222c5c224e6f64654e616d655c223a5c226c6979662d746573745c222c5c22576562736974655c223a5c22687474703a2f2f7777772e62616964752e636f6d5c222c5c2244657461696c735c223a5c2264657461696c735c222c5c2256616c696461746f725465726d5c223a307d2c7b5c224e6f646549645c223a5c2234396431306430323839616239313031653937376364376439373234653233343865393733663161366239313434313032623161323334663633633161363530343238636230396138326265376535623635653838666432366661393136366132626336653566646239393563396362303662323064376338646531393437615c222c5c22426c735075624b65795c223a5c223163643232323334626339323066306364333937636130663233373232646161303330646637386161323636386132366331616233373133323266356161656364333066373738663733306162653233613139316333346361613637373830663730363735626337626536646164333432303664616166643665646263616264623330383263643862333934663063343338363765326266396565646432616139366161613336346561643833643838666136633038363939306239313030665c222c5c225374616b696e67416464726573735c223a5c223078356535376165393765373134616265393930633838323337376161663963353766346561333633625c222c5c2242656e65666974416464726573735c223a5c223078313030303030303030303030303030303030303030303030303030303030303030303030303030335c222c5c225374616b696e675478496e6465785c223a302c5c2250726f6772616d56657273696f6e5c223a313739322c5c225374616b696e67426c6f636b4e756d5c223a302c5c225368617265735c223a5c2230783133646133323962363333363437313830303030305c222c5c2245787465726e616c49645c223a5c225c222c5c224e6f64654e616d655c223a5c22706c61746f6e2e6e6f64652e315c222c5c22576562736974655c223a5c227777772e706c61746f6e2e6e6574776f726b5c222c5c2244657461696c735c223a5c2254686520506c61744f4e204e6f64655c222c5c2256616c696461746f725465726d5c223a307d5d222c224572724d7367223a226f6b227d");
    return platonCall;
  }
}
