package com.platon.mtool.client;


import com.platon.mtool.client.parser.BaseOptionParser;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.client.tools.ResourceUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Created by liyf. */
@ExtendWith(MockitoExtension.class)
class CmdTest {

  private final static String ADDRESS = "lat1k92gm4sszzn59ntxlwrryj4nu2f4tpjttq6vtp";
  private static String WHITE_SPACE = "\\s+";
  private static String VALIDATOR_CONFIG_PATH =
      ResourceUtils.getResource("validator_config.json").toAbsolutePath().toString();
  private static String OBSERVED_KEYSTORE_PATH =
      ResourceUtils.getResource("staking_observed.json").toAbsolutePath().toString();
  private static String REWARD_CONFIG_PATH =
      ResourceUtils.getResource("reward_config.json").toAbsolutePath().toString();
  private static String KEYSTORE_PATH =
      ResourceUtils.getResource("staking.keystore").toAbsolutePath().toString();
  private static String RESTRICTING_PLAN_PATH =
          ResourceUtils.getResource("restricting_plans.json").toAbsolutePath().toString();

  private static String benefitAddress = "lat196278ns22j23awdfj9f2d4vz0pedld8anl5k3a";
  private static String nodeName = "nodeNameForTest";
  private static String website = "www.website.com";
  private static String externalId = "github_commitID";
  private static String details = "node_description";

  @InjectMocks private BaseOptionParser parser = new BaseOptionParser("mtool-test");

  @BeforeEach
  void setup(){
    CliConfigUtils.loadProperties();
  }
  @Test
  void create_restricting() {
    String args =
            String.format(
                    "create_restricting --keystore %s --config %s --file %s",
                    KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, RESTRICTING_PLAN_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeCreate_restricting() {
    String args =
            String.format(
                    "create_restricting --address %s --config %s --file %s",
                    OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, RESTRICTING_PLAN_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }


  @Test
  void staking() {
    String args =
        String.format(
            "staking --amount 5000000 --keystore %s --config %s --benefit_address %s  --delegated_reward_rate 5000 --node_name %s --external_id %s --website %s  --details %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, benefitAddress, nodeName, externalId, website, details);

    //args = "staking --amount 10000 --keystore D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\cdm.json --config D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator.json --delegated_reward_rate 900 --benefit_address lat1l3mv260zc0et6d65qks2hvevmv7t0577e760fj";
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void account_balance() {

    //mtool-client account list
   // String args = String.format("account balance D:\\javalang\\github.com\\mtool\\mtool-client\\src\\test\\resources\\keystore_huangyun.json --config D:\\javalang\\github.com\\mtool\\mtool-client\\src\\test\\resources\\validator_huangyun.json");
    String args = "account balance D:\\javalang\\github.com\\mtool\\mtool-client\\src\\test\\resources\\keystore_huangyun.json --config D:\\javalang\\github.com\\mtool\\mtool-client\\src\\test\\resources\\validator_huangyun.json";
    //args = "staking --amount 10000 --keystore D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\cdm.json --config D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator.json --delegated_reward_rate 900 --benefit_address lat1tafunymdk222q2wma3fmncnrkksnkys99hp3yu";
    parser.parse(args.split(WHITE_SPACE));
    new MtoolClient().run(parser);
    assertTrue(true);
  }

  @Test
  void account_list() {
    //mtool-client account list
    String args =
            String.format("account list");

    //args = "staking --amount 10000 --keystore D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\cdm.json --config D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator.json --delegated_reward_rate 900 --benefit_address lat1l3mv260zc0et6d65qks2hvevmv7t0577e760fj";
    parser.parse(args.split(WHITE_SPACE));
    new MtoolClient().run(parser);
    assertTrue(true);
  }

  @Test
  void auto_staking() {
    //mtool-client staking --config /home/luodahui/mtool/validator/validator_config_151_6790.json --keystore /home/luodahui/mtool/keystore/test_staking3.json --autoamount 10000 --benefit_address lat1tafunymdk222q2wma3fmncnrkksnkys99hp3yu --delegated_reward_rate 5000 --node_name myNode4 --website www.mywebsite4.com --details myNodeDescription4 --external_id 121412312
    KEYSTORE_PATH = ResourceUtils.getResource("test_staking_luodahui.json").toAbsolutePath().toString();
    VALIDATOR_CONFIG_PATH = ResourceUtils.getResource("validator_config_luodahui.json").toAbsolutePath().toString();
    benefitAddress = "lat1tafunymdk222q2wma3fmncnrkksnkys99hp3yu";
    nodeName = "myNode4 ";
    externalId = "121412312";
    website = "http://www.mywebsite4.com/";
    details = "myNodeDescription4";
    String args =
            String.format(
                    "staking --autoamount 100000 --keystore %s --config %s --benefit_address %s  --delegated_reward_rate 5000 --node_name %s --external_id %s --website %s  --details %s",
                    KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, benefitAddress, nodeName, externalId, website, details);

    //args = "staking --amount 10000 --keystore D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\cdm.json --config D:\\javalang\\Juzix-Platon\\mtool\\mtool-client\\src\\test\\resources\\validator.json --delegated_reward_rate 900 --benefit_address lat1vr8v48qjjrh9dwvdfctqauz98a7yp5scvd0pey";
    parser.parse(args.split(WHITE_SPACE));
    //new MtoolClient().run(parser);
    assertTrue(true);
  }

  @Test
  void staking_invalid_delegatedRewardPer() {
    String args =
            String.format(
                    "staking --amount 5000000 --keystore %s --config %s --benefit_address %s  --delegated_reward_rate 50000 --node_name %s --external_id %s --website %s --details %s",
                    KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, benefitAddress, nodeName, externalId, website, details);
    Assertions.assertThrows(com.beust.jcommander.ParameterException.class, ()-> parser.parse(args.split(WHITE_SPACE)));
  }

  @Test
  void observeStaking() {
    String args =
            String.format(
                    "staking --amount 5000000 --address %s --config %s --benefit_address %s  --delegated_reward_rate 5000 --node_name %s --external_id %s --website %s --details %s",
                    OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, benefitAddress, nodeName, externalId, website, details);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeStaking_invalid_delegatedRewardPer() {
    String args =
            String.format(
                    "staking --amount 5000000 --address %s --config %s --benefit_address %s  --delegated_reward_rate 50000 --node_name %s --external_id %s --website %s --details %s",
                    OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH, benefitAddress, nodeName, externalId, website, details);
    Assertions.assertThrows(com.beust.jcommander.ParameterException.class, ()-> parser.parse(args.split(WHITE_SPACE)));
  }

  @Test
  void declareVersion() {
    String args =
        String.format(
            "declare_version --keystore %s --config %s", KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeDeclareVersion() {
    String args =
        String.format(
            "declare_version --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void increaseStaking() {
    String args =
        String.format(
            "increasestaking --amount 5000000 --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeIncreaseStaking() {
    String args =
        String.format(
            "increasestaking --amount 5000000 --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void submitCancelProposal() {
    String args =
        String.format(
            "submit_cancelproposal --proposalid 5000000 --end_voting_rounds 12 --pid_id pidId --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeSubmitCancelProposal() {
    String args =
        String.format(
            "submit_cancelproposal --proposalid 5000000 --end_voting_rounds 12 --pid_id pidId --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void submitTextProposal() {
    String args =
        String.format(
            "submit_textproposal --pid_id pidId --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeSubmitTextProposal() {
    String args =
        String.format(
            "submit_textproposal --pid_id pidId --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void submitVersionProposal() {
    String args =
        String.format(
            "submit_versionproposal --newversion 1.0.0 --end_voting_rounds 10 --pid_id pidId --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeSubmitVersionProposal() {
    String args =
        String.format(
            "submit_versionproposal --newversion 1.0.0 --end_voting_rounds 10 --pid_id asdf --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void submitParamProposal() {
    String args =
        String.format(
            "submit_paramproposal --module module --paramname name --paramvalue value --pid_id pid --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeSubmitParamProposal() {
    String args =
        String.format(
            "submit_paramproposal --module module --paramname name --paramvalue value --pid_id pid --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void unstaking() {
    String args =
        String.format("unstaking --keystore %s --config %s", KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeUnstaking() {
    String args =
        String.format(
            "unstaking --address %s --config %s", OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void update_validator() {
    String args =
        String.format(
            "update_validator --node_name new_name --website new_website --delegated_reward_rate 6000  --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void update_validator_error() {
    String args =
            String.format(
                    "update_validator --keystore %s --config %s",
                    KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }


  @Test
  void observe_update_validator() {
    String args =
        String.format(
            "update_validator --node_name asdf --website asdf --external_id asdf --delegated_reward_rate 7000 --details asdf --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void update_validator2() {
    String args =
        String.format(
            "update_validator --node_name asdf --website asdf --external_id asdf --benefit_address %s --details asdf --keystore %s --config %s",
            ADDRESS,
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void voteCancelProposal() {
    String args =
        String.format(
            "vote_cancelproposal --proposalid asdf --opinion yes --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeVoteCancelProposal() {
    String args =
        String.format(
            "vote_cancelproposal --proposalid asdf --opinion yes --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void voteTextProposal() {
    String args =
        String.format(
            "vote_textproposal --proposalid asdf --opinion yes --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeVoteTextProposal() {
    String args =
        String.format(
            "vote_textproposal --proposalid asdf --opinion yes --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void voteVersionProposal() {
    String args =
        String.format(
            "vote_versionproposal --proposalid proposalid --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeVoteVersionProposal() {
    String args =
        String.format(
            "vote_versionproposal --proposalid proposalid --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void voteParamProposal() {
    String args =
        String.format(
            "vote_paramproposal --proposalid proposalid --opinion yes --keystore %s --config %s",
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeVoteParamProposal() {
    String args =
        String.format(
            "vote_paramproposal --proposalid proposalid --opinion yes --address %s --config %s",
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void accountCreateWallet() {
    String args = "account new liyf";
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void accountRecoverMnemonics() {
    String args = "account recover -m mnemonics";
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void accountRecoverPrivateKey() {
    String args = "account recover -k privateKey";
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void accountList() {
    String args = "account list";
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void accountBalance() {
    String args =
        String.format("account balance -a %s --config %s liyf",
            ADDRESS,
            VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void txTransfer() {
    String args =
        String.format(
            "tx transfer --recipient %s --keystore %s --config %s --amount 1",
            ADDRESS,
            KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }

  @Test
  void observeTxTransfer() {
    String args =
        String.format(
            "tx transfer --recipient %s --address %s --config %s --amount 1",
            ADDRESS,
            OBSERVED_KEYSTORE_PATH, VALIDATOR_CONFIG_PATH);
    parser.parse(args.split(WHITE_SPACE));
    assertTrue(true);
  }
}
