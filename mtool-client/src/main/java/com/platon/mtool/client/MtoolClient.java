package com.platon.mtool.client;

import com.beust.jcommander.MissingCommandException;
import com.beust.jcommander.ParameterException;
import com.platon.mtool.client.execute.*;
import com.platon.mtool.client.execute.observe.*;
import com.platon.mtool.client.execute.restricting.CreateRestrictingPlanExecutor;
import com.platon.mtool.client.parser.BaseOptionParser;
import com.platon.mtool.client.router.AccountRouter;
import com.platon.mtool.client.router.TxRouter;
import com.platon.mtool.client.tools.CliConfigUtils;
import com.platon.mtool.client.tools.PrintUtils;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.AllModules;
import com.platon.mtool.common.BuildInfo;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import com.platon.mtool.common.web3j.Keystore;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * mtool客户端程序入口
 *
 * <p>Created by liyf.
 */
public class MtoolClient {

  private static final Logger logger = LoggerFactory.getLogger(MtoolClient.class);

  public static void main(String[] args) {
    RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    List<String> arguments = runtimeMxBean.getInputArguments();
    LogUtils.info(logger, () -> Log.newBuilder().kv("jvm args", arguments));
    MDC.put("uuid", UUID.randomUUID().toString());
    Instant start = Instant.now();
    new MtoolClient().run(args);
    MDC.remove("uuid");
    Instant end = Instant.now();
    Duration duration = Duration.between(start, end);
    LogUtils.info(logger, () -> Log.newBuilder().kv("Execution time", duration.toMillis() + "ms"));
  }

  public void run(String[] args) {
    //加载用户自定义配置文件，加载hrp/chain id等
    CliConfigUtils.loadProperties();

    // 构建参数解析器
    BaseOptionParser parser = new BaseOptionParser("mtool");
    try {
      // 对传入的参数进行解析
      parser.parse(args);
    } catch (MissingCommandException e) {
      PrintUtils.echo("Command [%s] not found (use -h for help)", e.getUnknownCommand());
      System.exit(0);
    } catch (ParameterException e) {
      // 参数异常为命令字符串解析期间的报错。这里的处理都是为了把框架写死的错误提示语改成需求文档要求的文本
      if (e.getMessage() != null) {
        if (e.getMessage().contains("Was passed main parameter")) {
          String arg =
                  e.getMessage()
                          .replace("Was passed main parameter '", "")
                          .replace("' but no main parameter was defined in your arg class", "");
          PrintUtils.echo(
                  "command '%s' not support option '%s'", e.getJCommander().getParsedCommand(), arg);
        } else if (e.getMessage().contains("The following options are required")) {
          PrintUtils.echo(
                  e.getMessage()
                          .replace("The following options are required", "Missing required option"));
        } else if (e.getMessage().contains("The following option is required")) {
          PrintUtils.echo(
                  e.getMessage()
                          .replace("The following option is required", "Missing required option"));
        } else if (e.getMessage().contains("Expected a value after parameter")) {
          PrintUtils.echo(
                  e.getMessage().replace("Expected a value after parameter", "").trim()
                          + " can't be empty");
        } else {
          PrintUtils.echo(e.getMessage());
        }
      } else {
        PrintUtils.echo("unknow parameter exception");
      }
      System.exit(0);
    } catch (Exception e) {
      logger.error("command not found", e);
      PrintUtils.echo("command not found");
      StringBuilder sb = new StringBuilder(128);
      parser.getjCommander().usage(sb);
      PrintUtils.echo(sb.toString());
      System.exit(0);
    }
    run(parser);
  }

  /**
   * IMPORTANT:
   * 对于在unit test中，直接调用此方法前，
   * 要先执行CliConfigUtils.loadProperties();
   * 以便加载自定义链参数
   * @param parser
   */
  public void run(BaseOptionParser parser){

    if (StringUtils.isEmpty(parser.getCommandName())) {
      // 如果命令命令， 看看选项是否是输出帮助或者输出版本
      if (parser.getRootOption().isHelp()) {
        StringBuilder sb = new StringBuilder(128);
        parser.getjCommander().usage(sb);
        PrintUtils.echo(sb.toString().replaceAll("Possible Values: .*", ""));
      } else if (parser.getRootOption().isShowVersion()) {
        PrintUtils.echo(BuildInfo.info());
      }
    } else {
      CliExecutor<?> cliExecutor;
      Keystore keystore;
      // 执行器路由逻辑
      switch (parser.getCommandName()) {
        case AllCommands.CREATE_RESTRICTING:
          keystore = parser.getCreateRestrictingPlanOption().getKeystore();
          // 条件判断传入的钱包是否观察钱包， 执行对应的执行器
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveCreateRestrictingPlanExecutor(parser.getSubCommander(), parser.getCreateRestrictingPlanOption());
          } else {
            cliExecutor = new CreateRestrictingPlanExecutor(parser.getSubCommander(), parser.getCreateRestrictingPlanOption());
          }
          break;
        case AllCommands.STAKING:
          keystore = parser.getStakingOption().getKeystore();
          // 条件判断传入的钱包是否观察钱包， 执行对应的执行器
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveStakingExecutor(parser.getSubCommander(), parser.getStakingOption());
          } else {
            cliExecutor = new StakingExecutor(parser.getSubCommander(), parser.getStakingOption());
          }
          break;
        case AllCommands.DECLARE_VERSION:
          keystore = parser.getDeclareVersionOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveDeclareVersionExecutor(
                            parser.getSubCommander(), parser.getDeclareVersionOption());
          } else {
            cliExecutor =
                    new DeclareVersionExecutor(
                            parser.getSubCommander(), parser.getDeclareVersionOption());
          }
          break;
        case AllCommands.INCREASESTAKING:
          keystore = parser.getIncreaseStakingOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveIncreaseStakingExecutor(
                            parser.getSubCommander(), parser.getIncreaseStakingOption());
          } else {
            cliExecutor =
                    new IncreaseStakingExecutor(
                            parser.getSubCommander(), parser.getIncreaseStakingOption());
          }
          break;
        case AllCommands.SUBMIT_VERSION_PROPOSAL:
          keystore = parser.getSubmitVersionProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveSubmitVersionProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitVersionProposalOption());
          } else {
            cliExecutor =
                    new SubmitVersionProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitVersionProposalOption());
          }
          break;
        case AllCommands.SUBMIT_TEXT_PROPOSAL:
          keystore = parser.getSubmitTextProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveSubmitTextproposalExecutor(
                            parser.getSubCommander(), parser.getSubmitTextProposalOption());
          } else {
            cliExecutor =
                    new SubmitTextProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitTextProposalOption());
          }
          break;
        case AllCommands.SUBMIT_CANCEL_PROPOSAL:
          keystore = parser.getSubmitCancelProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveSubmitCancelproposalExecutor(
                            parser.getSubCommander(), parser.getSubmitCancelProposalOption());
          } else {
            cliExecutor =
                    new SubmitCancelProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitCancelProposalOption());
          }
          break;
        case AllCommands.SUBMIT_PARAM_PROPOSAL:
          keystore = parser.getSubmitParamProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveSubmitParamProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitParamProposalOption());
          } else {
            cliExecutor =
                    new SubmitParamProposalExecutor(
                            parser.getSubCommander(), parser.getSubmitParamProposalOption());
          }
          break;
        case AllCommands.UNSTAKING:
          keystore = parser.getUnstakingOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveUnstakingExecutor(parser.getSubCommander(), parser.getUnstakingOption());
          } else {
            cliExecutor =
                    new UnstakingExecutor(parser.getSubCommander(), parser.getUnstakingOption());
          }
          break;
        case AllCommands.UPDATE_VALIDATOR:
          keystore = parser.getUpdateValidatorOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveUpdateValidatorExecutor(
                            parser.getSubCommander(), parser.getUpdateValidatorOption());
          } else {
            cliExecutor =
                    new UpdateValidatorExecutor(
                            parser.getSubCommander(), parser.getUpdateValidatorOption());
          }
          break;
        case AllCommands.VOTE_VERSION_PROPOSAL:
          keystore = parser.getVoteVersionProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveVoteVersionProposalExecutor(
                            parser.getSubCommander(), parser.getVoteVersionProposalOption());
          } else {
            cliExecutor =
                    new VoteVersionProposalExecutor(
                            parser.getSubCommander(), parser.getVoteVersionProposalOption());
          }
          break;
        case AllCommands.VOTE_TEXT_PROPOSAL:
          keystore = parser.getVoteTextProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveVoteTextProposalExecutor(
                            parser.getSubCommander(), parser.getVoteTextProposalOption());
          } else {
            cliExecutor =
                    new VoteTextProposalExecutor(
                            parser.getSubCommander(), parser.getVoteTextProposalOption());
          }
          break;
        case AllCommands.VOTE_CANCEL_PROPOSAL:
          keystore = parser.getVoteCancelProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveVoteCancelProposalExecutor(
                            parser.getSubCommander(), parser.getVoteCancelProposalOption());
          } else {
            cliExecutor =
                    new VoteCancelProposalExecutor(
                            parser.getSubCommander(), parser.getVoteCancelProposalOption());
          }
          break;
        case AllCommands.VOTE_PARAM_PROPOSAL:
          keystore = parser.getVoteParamProposalOption().getKeystore();
          if (keystore != null && keystore.getType().equals(Keystore.Type.OBSERVE)) {
            cliExecutor =
                    new ObserveVoteParamProposalExecutor(
                            parser.getSubCommander(), parser.getVoteParamProposalOption());
          } else {
            cliExecutor =
                    new VoteParamProposalExecutor(
                            parser.getSubCommander(), parser.getVoteParamProposalOption());
          }
          break;
        case AllCommands.CREATE_OBSERVEWALLET:
          cliExecutor =
                  new CreateObserveWalletExecutor(
                          parser.getSubCommander(), parser.getCreateObserveWalletOption());
          break;
        case AllCommands.OFFLINESIGN:
          cliExecutor =
                  new OfflineSignExcutor(parser.getSubCommander(), parser.getOfflineSignOption());
          break;
        case AllCommands.SEND_SIGNEDTX:
          cliExecutor =
                  new SendSignedTxExecutor(parser.getSubCommander(), parser.getSendSignedTxOption());
          break;
        // 模块命令会传入相应的路由解析， 比普通命令多一层路由
        case AllModules.ACCOUNT:
          cliExecutor = new AccountRouter(parser.getSubCommander(), parser.getAccountOptions());
          break;
        case AllModules.TX:
          cliExecutor = new TxRouter(parser.getSubCommander(), parser.getTxOptions());
          break;
        default:
          throw new IllegalStateException("Unexpected value: " + parser.getCommandName());
      }
      cliExecutor.execute();
    }
  }

}
