package com.example.hmsintegracion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

public class RemoteConfig extends AppCompatActivity {

    private static final String GREETING_KEY = "GREETING_KEY";
    private static final String SET_BOLD_KEY = "SET_BOLD_KEY";
    private AGConnectConfig config;
    private TextView tv;
    private Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_config);

        tv= (TextView)findViewById(R.id.textView);
        b1=(Button)findViewById(R.id.buttonRC);

        //Initialize a Remote Configuration object instance
        config = AGConnectConfig.getInstance();

        config.applyDefault(R.xml.remote_config);
        tv.setText(config.getValueAsString(GREETING_KEY));
        Boolean isBold = config.getValueAsBoolean(SET_BOLD_KEY);
        if (isBold){
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchAndApply();
            }
        });
    }

    private void fetchAndApply(){
        config.fetch().addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);
                updateUI();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                tv.setText("fetch setting failed: " + e.getMessage());
            }
        });
    }

    private void updateUI(){
        String text = config.getValueAsString(GREETING_KEY);
        Boolean isBold = config.getValueAsBoolean(SET_BOLD_KEY);
        tv.setText(text);
        if (isBold){
            tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
    }
}