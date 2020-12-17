package com.platon.mtool.client.execute.restricting;

import com.alaya.contracts.ppos.RestrictingPlanContract;
import com.alaya.contracts.ppos.dto.RestrictingPlan;
import com.alaya.contracts.ppos.dto.TransactionResponse;
import com.alaya.contracts.ppos.dto.enums.GovernParamItemSupported;
import com.alaya.contracts.ppos.dto.enums.StakingAmountType;
import com.alaya.contracts.ppos.dto.req.CreateRestrictingParam;
import com.alaya.crypto.Credentials;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.methods.response.PlatonSendTransaction;
import com.alaya.tx.gas.GasProvider;
import com.alaya.utils.Convert;
import com.beust.jcommander.JCommander;
import com.platon.mtool.client.execute.MtoolExecutor;
import com.platon.mtool.client.options.restricting.CreateRestrictingPlanOption;
import com.platon.mtool.client.service.BlockChainService;
import com.platon.mtool.client.tools.ProgressBar;
import com.platon.mtool.common.AllCommands;
import com.platon.mtool.common.entity.ValidatorConfig;
import com.platon.mtool.common.exception.MtoolClientException;
import com.platon.mtool.common.logger.Log;
import com.platon.mtool.common.utils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

public class CreateRestrictingPlanExecutor extends MtoolExecutor<CreateRestrictingPlanOption> {

    private static final Logger logger = LoggerFactory.getLogger(CreateRestrictingPlanExecutor.class);

    private BlockChainService blockChainService = BlockChainService.singleton();

    public CreateRestrictingPlanExecutor(JCommander commander, CreateRestrictingPlanOption commonOption) {
        super(commander, commonOption);
    }

    protected Web3j getWeb3j(ValidatorConfig validatorConfig) {
        return com.platon.mtool.common.web3j.Web3jUtil.getFromConfig(validatorConfig);
    }

    public RestrictingPlanContract getRestrictingPlanContract(
            Web3j web3j, Credentials credentials, Long chainId) {
        return RestrictingPlanContract.load(web3j, credentials, chainId);
    }

    @Override
    public void execute(CreateRestrictingPlanOption option) throws Exception {
        LogUtils.info(logger, () -> Log.newBuilder().msg(AllCommands.CREATE_RESTRICTING).kv("option", option));
        ProgressBar.start();

        //初始化web3j
        ValidatorConfig validatorConfig = option.getConfig();
        Web3j web3j = getWeb3j(validatorConfig);

        //查询链上的治理参数值（创建锁仓计划时，每次释放金额的最小值）
        BigInteger minimumReleaseAtp = BigInteger.ZERO;
        String paramValue = blockChainService.getGovernParamValue(web3j, GovernParamItemSupported.Restricting_minimumRelease);
        if (StringUtils.isNotBlank(paramValue)) {
            minimumReleaseAtp = Convert.fromVon(paramValue, Convert.Unit.ATP).toBigInteger();
            if(minimumReleaseAtp.signum()<=0){
                throw new MtoolClientException("invalid minimum amount of restricting release");
            }

        }else{
            throw new MtoolClientException("cannot find the minimum amount of restricting release");
        }
        //总锁仓金额
        BigInteger totalVons = BigInteger.ZERO;
        if (option.getRestrictingConfig()!=null){
            for(RestrictingPlan plan : option.getRestrictingConfig().getPlans()){
                //锁仓计划amoount的单位是ATP
                BigInteger atpAmount = plan.getAmount();
                BigInteger vonAmount = Convert.toVon(atpAmount.toString(), Convert.Unit.ATP).toBigInteger();
                plan.setAmount(vonAmount);

                if (atpAmount.compareTo(minimumReleaseAtp)<0){
                    throw new MtoolClientException("plan item amount less than " + minimumReleaseAtp +"(ATP)");
                }
                totalVons = totalVons.add(vonAmount);
                /*if(plan.getEpoch().signum()<=0){
                    throw new MtoolClientException("plan item epoch is less than 1");
                }*/
            }
        }


        //加载合约JAVA包装类
        Credentials credentials = option.getKeystore().getCredentials();
        RestrictingPlanContract restrictingPlanContract = getRestrictingPlanContract(web3j, credentials, CLIENT_CONFIG.getTargetChainId());

        //准备调用合约方法的参数
        CreateRestrictingParam createRestrictingParam =  new CreateRestrictingParam();
        createRestrictingParam.setAccount(option.getRestrictingConfig().getAccount());
        createRestrictingParam.setPlans(option.getRestrictingConfig().getPlans());

        //准备gasLimit/gasPrice
        GasProvider gasProvider = restrictingPlanContract.getCreateRestrictingPlanGasProvider(createRestrictingParam);

        //检查账户余额是否够付手续费（gasLimit*gasPrice)
        blockChainService.validBalanceEnough(option.getKeystore().getAddress(), totalVons, gasProvider, web3j, StakingAmountType.FREE_AMOUNT_TYPE);

        //准备交易对象
        PlatonSendTransaction transaction = restrictingPlanContract.createRestrictingPlanReturnTransaction(createRestrictingParam.getAccount(), Arrays.asList(createRestrictingParam.getPlans()), gasProvider).send();

        //发送交易
        TransactionResponse response = restrictingPlanContract.getTransactionResponse(transaction).send();

        LogUtils.info(logger, () -> Log.newBuilder().msg(AllCommands.CREATE_RESTRICTING).kv("transaction", transaction));
        LogUtils.info(logger, () -> Log.newBuilder().msg(AllCommands.CREATE_RESTRICTING).kv("response", response));

        ProgressBar.stop();
    }
}
