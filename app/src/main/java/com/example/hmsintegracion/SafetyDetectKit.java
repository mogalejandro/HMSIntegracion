package com.example.hmsintegracion;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.entity.core.CommonCode;
import com.huawei.hms.support.api.entity.safetydetect.MaliciousAppsData;
import com.huawei.hms.support.api.entity.safetydetect.MaliciousAppsListResp;
import com.huawei.hms.support.api.entity.safetydetect.UrlCheckResponse;
import com.huawei.hms.support.api.entity.safetydetect.UrlCheckThreat;
import com.huawei.hms.support.api.entity.safetydetect.WifiDetectResponse;
import com.huawei.hms.support.api.safetydetect.SafetyDetect;
import com.huawei.hms.support.api.safetydetect.SafetyDetectClient;
import com.huawei.hms.support.api.safetydetect.SafetyDetectStatusCodes;

import java.util.List;

public class SafetyDetectKit extends AppCompatActivity implements View.OnClickListener {

    public final static String TAG = "safety";
    TextView wf1,wf2,wf3;
    public static final int MALWARE = 1;
    public static final int PHISHING = 3;
    private SafetyDetectClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_safety_detect_kit);

        wf1 = findViewById(R.id.textViewWifi);
        wf2 = findViewById(R.id.textViewWifi3);
        wf3 = findViewById(R.id.textViewWifi2);
        findViewById(R.id.buttonW1).setOnClickListener(this);
        findViewById(R.id.buttonW).setOnClickListener(this);
        findViewById(R.id.buttonW2).setOnClickListener(this);

        //CLIENTE SAFETYDETECT
        client = SafetyDetect.getClient(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonW1:
                invokeGetWifiDetectStatus();
                break;
            case R.id.buttonW:
                callUrlCheckApi();
                break;
            case R.id.buttonW2:
                invokeGetMaliciousApps();
                break;
            default:
                break;
        }
    }

    private void invokeGetWifiDetectStatus() {
        Log.i(TAG, "Start to getWifiDetectStatus!");
        //inicializar nuestro cliente safetydetect
        SafetyDetectClient wifidetectClient = SafetyDetect.getClient(this);
        Task task = wifidetectClient.getWifiDetectStatus();
        task.addOnSuccessListener(new OnSuccessListener<WifiDetectResponse>() {
            @Override
            public void onSuccess(WifiDetectResponse wifiDetectResponse) {
                int wifiDetectStatus = wifiDetectResponse.getWifiDetectStatus();
                Log.i(TAG, "\n-1: Failed to obtain the Wi-Fi status. \n" + "0: No Wi-Fi is connected. \n" + "1: The connected Wi-Fi is secure. \n" + "2: The connected Wi-Fi is insecure.");
                Log.i(TAG, "wifiDetectStatus is: " + wifiDetectStatus);
                wf1.setText("wifiDetectStatus is: " +wifiDetectStatus);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.e(TAG,"Error: " + apiException.getStatusCode() + ":"+ SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " + apiException.getStatusMessage());
                    wf1.setText("Error: " + apiException.getStatusCode() + ":"+ SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " + apiException.getStatusMessage());
                } else {
                    Log.e(TAG, "ERROR! " + e.getMessage());
                }
            }
        });
    }

    private void callUrlCheckApi() {


        Log.i(TAG, "Start call URL check api");
        String url = "https://stackoverflow.com/questions/17727645/how-to-update-gradle-in-android-studio";

        String appid=  AGConnectServicesConfig.fromContext(this).getString("client/app_id");
        String appid2= "104483069";

        client.urlCheck(url,appid,UrlCheckThreat.MALWARE, UrlCheckThreat.PHISHING)
                .addOnSuccessListener(new OnSuccessListener<UrlCheckResponse>() {

                    @Override
                    public void onSuccess(UrlCheckResponse urlCheckResponse) {
                        List<UrlCheckThreat> list = urlCheckResponse.getUrlCheckResponse();
                        if (list.isEmpty()) {
                            // No threats found.
                            wf2.setText("No threats found.");
                        } else {
                            // Threats found!
                            wf2.setText("Threats found!");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // An error with the Huawei Mobile Service API contains some additional details.
                        String errorMsg;
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            errorMsg = "Error: " +
                                    SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " +
                                    e.getMessage();
                        } else {
                            errorMsg = e.getMessage();
                        }
                        Log.d(TAG, errorMsg);
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void invokeGetMaliciousApps() {
        SafetyDetectClient appsCheckClient = SafetyDetect.getClient(this);
        Task task = appsCheckClient.getMaliciousAppsList();
        task.addOnSuccessListener(new OnSuccessListener<MaliciousAppsListResp>() {
            @Override
            public void onSuccess(MaliciousAppsListResp maliciousAppsListResp) {
                // Indicates that communication with the service was successful.
                // Use resp.getMaliciousApps() to get malicious apps data.
                List<MaliciousAppsData> appsDataList = maliciousAppsListResp.getMaliciousAppsList();
                // Indicates get malicious apps was successful.
                if(maliciousAppsListResp.getRtnCode() == CommonCode.OK) {
                    if (appsDataList.isEmpty()) {
                        // Indicates there are no known malicious apps.
                        Log.i(TAG, "There are no known potentially malicious apps installed.");
                        wf3.setText("There are no known potentially malicious apps installed");
                    } else {
                        Log.i(TAG, "Potentially malicious apps are installed!");
                        for (MaliciousAppsData maliciousApp : appsDataList) {
                            Log.i(TAG, "Information about a malicious app:");
                            wf3.setText("APK: " + maliciousApp.getApkPackageName());
                            // Use getApkPackageName() to get APK name of malicious app.
                            Log.i(TAG, "APK: " + maliciousApp.getApkPackageName());
                            // Use getApkSha256() to get APK sha256 of malicious app.
                            Log.i(TAG, "SHA-256: " + maliciousApp.getApkSha256());
                            // Use getApkCategory() to get category of malicious app.
                            // Categories are defined in AppsCheckConstants
                            Log.i(TAG, "Category: " + maliciousApp.getApkCategory());
                        }
                    }
                }else{
                    Log.e(TAG,"getMaliciousAppsList failed: "+maliciousAppsListResp.getErrorReason());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // An error occurred during communication with the service.
                if (e instanceof ApiException) {
                    // An error with the HMS API contains some
                    // additional details.
                    ApiException apiException = (ApiException) e;
                    // You can retrieve the status code using the apiException.getStatusCode() method.
                    Log.e(TAG, "Error: " +  SafetyDetectStatusCodes.getStatusCodeString(apiException.getStatusCode()) + ": " + apiException.getStatusMessage());
                } else {
                    // A different, unknown type of error occurred.
                    Log.e(TAG, "ERROR: " + e.getMessage());
                }
            }
        });
    }

}