package com.test.e_passportreader;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MenuItem;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import at.nineyards.anyline.models.AnylineImage;
import at.nineyards.anyline.modules.AnylineBaseModuleView;

abstract public class ScanActivity extends ScanningConfigurationActivity {
    public abstract Rect getCutoutRect();

    /**
     * @return the actual used {@link AnylineBaseModuleView}
     */
    protected abstract AnylineBaseModuleView getScanView();

    /**

     */
    protected abstract ScanModuleEnum.ScanModule getScanModule();
    protected long timeStarted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);



    }

    @Override
    protected void onResume() {
        super.onResume();
        resetTime();

    }

    /**
     * resets the time used the calculate how many seconds the scan required from startScanning() until a result has been reported
     */
    protected void resetTime() {
        timeStarted = System.currentTimeMillis();
    }

    protected long milliSecondsPassedSinceStartedScanning() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - timeStarted);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // overridePendingTransition(R.anim.fade_in, R.anim.activity_close_translate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
           // overridePendingTransition(R.anim.fade_in, R.anim.activity_close_translate);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();

    }



    protected String setupImagePath(AnylineImage image){
        String imagePath = "";
        try {
            if(this.getExternalFilesDir(null) != null) {

                imagePath = this
                        .getExternalFilesDir(null)
                        .toString() + "/results/" + "mrz_image";

            }else if(this.getFilesDir() != null){

                imagePath = this
                        .getFilesDir()
                        .toString() + "/results/" + "mrz_image";

            }
            File fullFile = new File(imagePath);
            //create the directory
            fullFile.mkdirs();
            image.save(fullFile, 100);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return imagePath;
    }

    protected void startScanResultIntent(String scanMode, HashMap<String, String> scanResult, String path){
        // String path = setupImagePath(anylineOcrResult.getCutoutImage());

        Intent i = new Intent(getBaseContext(), ScanViewResultActivity.class);
        i.putExtra(Constant.SCAN_MODULE, scanMode);
        i.putExtra(Constant.SCAN_RESULT_DATA, scanResult);
        i.putExtra(Constant.SCAN_FULL_PICTURE_PATH, path);

        startActivity(i);
    }

}
