package com.example.hmsintegracion;

import com.huawei.hms.push.HmsMessageService;

public class MyPushService extends HmsMessageService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
}
