package com.guangnian.mylibrary.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.guangnian.mylibrary.R;
import com.guangnian.mylibrary.wxapi.PayWechatManager;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        String app_id="";
        String mch_id="";
        String api_key="";
        String order_no=UUID.randomUUID().toString().replace("-", "");
        String productfeeName="";
        String price="";
        String callBackUrll="";
        String spbill_create_ip="";
        api = WXAPIFactory.createWXAPI(this, app_id);
        api.handleIntent(getIntent(), this);
        //订单编号
        new PayWechatManager(this).toWeChatPay(app_id, mch_id,
                api_key, order_no, productfeeName, price,
                callBackUrll, null,spbill_create_ip);
    }

    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(this, "支付回调", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            //回调结果
            if (resp.errCode == 0) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_LONG).show();
            } else if (resp.errCode == -2) {
                Toast.makeText(this, "支付取消", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
}