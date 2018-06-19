package com.test.e_passportreader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.util.LinkedHashMap;

import at.nineyards.anyline.camera.CameraController;
import at.nineyards.anyline.camera.CameraOpenListener;
import at.nineyards.anyline.modules.AnylineBaseModuleView;
import at.nineyards.anyline.modules.mrz.Identification;
import at.nineyards.anyline.modules.mrz.MrzResult;
import at.nineyards.anyline.modules.mrz.MrzResultListener;
import at.nineyards.anyline.modules.mrz.MrzScanView;

public class MainActivity extends ScanActivity implements CameraOpenListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private MrzScanView mrzScanView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
            }
        }
        getSupportActionBar().hide();
        getLayoutInflater().inflate(R.layout.activity_main, (ViewGroup) findViewById(R.id.scan_view_placeholder));
       // Toast.makeText(this, TAG, Toast.LENGTH_SHORT).show();
        mrzScanView = (MrzScanView) findViewById(R.id.mrz_view);

        // add a camera open listener that will be called when the camera is opened or an error occurred
        //  this is optional (if not set a RuntimeException will be thrown if an error occurs)
        mrzScanView.setCameraOpenListener(this);

        // the view can be configured via a json file in the assets, and this config is set here
        // (alternatively it can be configured via xml, see the Energy Example for that)
        //mrzScanView.setConfigFromAsset("mrz_view_config.json");
        //Log.d(TAG, "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        // initialize Anyline with the license key and a Listener that is called if a result is found
        mrzScanView.initAnyline(getString(R.string.anyline_license_key), new MrzResultListener() {

            @Override
            public void onResult(MrzResult mrzResult) {
                // This is called when a result is found.
                // The Identification includes all the data read from the MRZ
                // as scanned and the given image shows the scanned ID/Passport
                Identification identification = mrzResult.getResult();
                identification.toJSONObject();

                //set the path of the mrz Image
                String path = setupImagePath(mrzResult.getCutoutImage());
                startScanResultIntent(getResources().getString(R.string.title_mrz), getIdentificationResult(identification), path);
                setupScanProcessView(MainActivity.this, mrzResult, getScanModule());

            }
        });

    }

    @Override
    protected AnylineBaseModuleView getScanView() {
        return mrzScanView;
    }

    @Override
    public Rect getCutoutRect() {
        return mrzScanView.getCutoutRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start the actual scanning
        mrzScanView.startScanning();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //stop the scanning
        mrzScanView.cancelScanning();
        //release the camera (must be called in onPause, because there are situations where
        // it cannot be auto-detected that the camera should be released)
        mrzScanView.releaseCameraInBackground();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onCameraOpened(CameraController cameraController, int width, int height) {
        //the camera is opened async and this is called when the opening is finished
        Log.d(TAG, "Camera opened successfully. Frame resolution " + width + " x " + height);
    }

    @Override
    public void onCameraError(Exception e) {
        //This is called if the camera could not be opened.
        // (e.g. If there is no camera or the permission is denied)
        // This is useful to present an alternative way to enter the required data if no camera exists.
        throw new RuntimeException(e);
    }

    @Override
    protected ScanModuleEnum.ScanModule getScanModule() {
        return ScanModuleEnum.ScanModule.MRZ;
    }

    public LinkedHashMap<String, String> getIdentificationResult(Identification identification) {

        LinkedHashMap<String, String> identificationResult = new LinkedHashMap<>();

        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
        identificationResult.put(getResources().getString(R.string.mrz_document_type) , (identification.getDocumentType() == null || identification.getDocumentType().isEmpty()) ?  getResources().getString(R.string.not_available) : identification.getDocumentType());
        identificationResult.put(getResources().getString(R.string.mrz_country_code), (identification.getNationalityCountryCode() == null || identification.getNationalityCountryCode().isEmpty()) ? getResources().getString(R.string.not_available) : identification.getNationalityCountryCode());
        identificationResult.put(getResources().getString(R.string.mrz_document_number), (identification.getDocumentNumber() == null || identification.getDocumentNumber().isEmpty()) ? getResources().getString(R.string.not_available) : identification.getDocumentNumber());
        identificationResult.put(getResources().getString(R.string.mrz_sur_names),(identification.getSurNames() == null || identification.getSurNames().isEmpty()) ? getResources().getString(R.string.not_available) : identification.getSurNames());
        identificationResult.put(getResources().getString(R.string.mrz_given_names),(identification.getGivenNames() == null || identification.getGivenNames().isEmpty()) ? getResources().getString(R.string.not_available) : identification.getGivenNames());
        identificationResult.put(getResources().getString(R.string.mrz_expiration_date),(identification.getExpirationDateObject() == null) ? getResources().getString(R.string.not_available) : dateFormat.format(identification.getExpirationDateObject()));
        identificationResult.put(getResources().getString(R.string.mrz_date_of_birthday),(identification.getDayOfBirthObject() == null) ? getResources().getString(R.string.not_available) : dateFormat.format(identification.getDayOfBirthObject()));
        identificationResult.put(getResources().getString(R.string.mrz_sex), (identification.getSex() == null || identification.getSex().isEmpty()) ?  getResources().getString(R.string.not_available) : identification.getSex());

        return identificationResult;
    }






}
