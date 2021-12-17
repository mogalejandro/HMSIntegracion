package com.example.hmsintegracion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class AccountKit extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "Account";
    private HuaweiIdAuthService mAuthManager;
    private HuaweiIdAuthParams mAuthParam;
    public static final int REQUEST_SIGN_IN_LOGIN = 1002;
    TextView tva4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_kit);

        findViewById(R.id.buttonA).setOnClickListener(this::onClick);
        findViewById(R.id.buttonB).setOnClickListener(this::onClick);
        tva4=(TextView)findViewById(R.id.textView4);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonA:
                signIn();
                break;
            case R.id.buttonB:
                signOut();
                break;
            default:
                break;
        }
    }

    private void signIn() {
        mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(AccountKit.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), REQUEST_SIGN_IN_LOGIN);

    }


    private void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
                tva4.setText("signOut Success");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
                tva4.setText("signOut fail");
            }
        });
    }
}