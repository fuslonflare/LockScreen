package com.phuwarin.lockscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName() + "TAG";
    private static final int RESULT_ENABLE = 1;

    private AppCompatButton buttonEnableAdmin;
    private AppCompatButton buttonDisableAdmin;
    private AppCompatButton buttonLockScreen;

    private DevicePolicyManager deviceManger;
    private ActivityManager activityManager;
    private ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceManger = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        initUi();
    }

    private void initUi() {
        buttonEnableAdmin = findViewById(R.id.button_enable_admin);
        buttonDisableAdmin = findViewById(R.id.button_disable_admin);
        buttonLockScreen = findViewById(R.id.button_lock_screen);

        if (!deviceManger.isAdminActive(compName)) {
            buttonLockScreen.setEnabled(false);
        }

        if (deviceManger.isAdminActive(compName)) {
            buttonEnableAdmin.setEnabled(false);
            buttonDisableAdmin.setEnabled(true);
        } else {
            buttonEnableAdmin.setEnabled(true);
            buttonDisableAdmin.setEnabled(false);
        }
    }

    public void enableAdminPermission(View view) {
        updateButtonStates();

        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added.");

        startActivityForResult(intent, RESULT_ENABLE);
    }

    public void disableAdminPermission(View view) {
        updateButtonStates();
        deviceManger.removeActiveAdmin(compName);
    }

    public void lockScreen(View view) {
        if (deviceManger.isAdminActive(compName)) {
            deviceManger.lockNow();
        } else {
            Toast.makeText(this, "Admin Permission isn't active", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(TAG, "Admin enabled!");
                } else {
                    Log.i(TAG, "Admin enable FAILED!");
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateButtonStates() {
        buttonEnableAdmin.setEnabled(!buttonEnableAdmin.isEnabled());
        buttonDisableAdmin.setEnabled(!buttonDisableAdmin.isEnabled());
    }
}
