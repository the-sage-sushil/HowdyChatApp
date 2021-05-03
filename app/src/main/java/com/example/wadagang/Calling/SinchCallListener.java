package com.example.wadagang.Calling;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

public class SinchCallListener implements CallListener, CallClientListener {
    @Override
    public void onCallEnded(Call endedCall) {
        //call ended by either party
    }

    @Override
    public void onCallEstablished(Call establishedCall) {
        //incoming call was picked up
    }

    @Override
    public void onCallProgressing(Call progressingCall) {
        //call is ringing
    }

    @Override
    public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        //don't worry about this right now
    }

    @Override
    public void onIncomingCall(CallClient callClient, Call call) {

    }
}