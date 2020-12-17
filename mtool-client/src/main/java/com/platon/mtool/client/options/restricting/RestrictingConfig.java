package com.platon.mtool.client.options.restricting;

import com.alaya.contracts.ppos.dto.RestrictingPlan;

public class RestrictingConfig {
    private  String account;
    private RestrictingPlan[] plans;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public RestrictingPlan[] getPlans() {
        return plans;
    }

    public void setPlans(RestrictingPlan[] plans) {
        this.plans = plans;
    }


}

