package com.platon.mtool.client.test;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import com.alaya.contracts.ppos.RestrictingPlanContract;
import com.alaya.contracts.ppos.dto.BaseResponse;
import java.io.IOException;
import org.mockito.CheckReturnValue;
import com.alaya.protocol.Web3j;
import com.alaya.protocol.core.RemoteCall;
import com.alaya.protocol.core.Request;
import com.alaya.protocol.core.Response;

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
