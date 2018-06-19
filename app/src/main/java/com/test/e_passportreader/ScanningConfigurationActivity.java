package com.test.e_passportreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import at.nineyards.anyline.models.AnylineScanResult;
import at.nineyards.anyline.modules.AnylineBaseModuleView;

public abstract class ScanningConfigurationActivity extends AppCompatActivity {

    protected abstract AnylineBaseModuleView getScanView();


    protected abstract ScanModuleEnum.ScanModule getScanModule();


    protected <T extends AnylineScanResult> void setupScanProcessView(Context context, T result, ScanModuleEnum.ScanModule scanModule){


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  static void setupScanProcessView(Context context, String result, ScanModuleEnum.ScanModule scanModule, Bitmap bmp) {


        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setupScanResult(){

    }
}
