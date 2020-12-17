package com.platon.mtool.client.tools;

import com.alaya.contracts.ppos.StakingContract;
import com.alaya.contracts.ppos.dto.resp.Node;
import com.alaya.protocol.Web3j;

import static com.platon.mtool.client.tools.CliConfigUtils.CLIENT_CONFIG;

public class ContractUtil {
    public Node getNode(Web3j web3j, String nodeId) throws Exception {
        StakingContract stakingContract = StakingContract.load(web3j, CLIENT_CONFIG.getTargetChainId());
        Node node = stakingContract.getStakingInfo(nodeId).send().getData();
        return node;
    }
}
