package com.platon.mtool.client.test;

import com.platon.contracts.ppos.RestrictingPlanContract;
import com.platon.contracts.ppos.dto.BaseResponse;
import com.platon.protocol.Web3j;
import com.platon.protocol.core.RemoteCall;
import com.platon.protocol.core.Request;
import com.platon.protocol.core.Response;
import org.mockito.CheckReturnValue;

import java.io.IOException;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/** Created by liyf. */
public abstract class PlatonMockHelp {

  @CheckReturnValue
  public static Web3j mockWeb3j(Web3j mockWeb3j, Response response) throws IOException {
    Request platonRequest = mock(Request.class);
    doReturn(response).when(platonRequest).send();
    return doReturn(platonRequest).when(mockWeb3j);
  }

  @CheckReturnValue
  public static RestrictingPlanContract mockRestrictingPlanContract(
      RestrictingPlanContract mockContract, BaseResponse response) throws Exception {
    RemoteCall remoteCall = mock(RemoteCall.class);
    doReturn(response).when(remoteCall).send();
    return doReturn(remoteCall).when(mockContract);
  }
}
