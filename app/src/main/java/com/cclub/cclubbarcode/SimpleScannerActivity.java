package com.cclub.cclubbarcode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.content.ContentValues.TAG;

/**
 * Created by ivahmet on 02/04/2018.
 */

//Using a library to scan barcodes or qrs.
public class SimpleScannerActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    //Returning the barcode in string form to main activity.
    @Override
    public void handleResult(Result rawResult) {

        Log.v(TAG, rawResult.getText());
        Log.v(TAG, rawResult.getBarcodeFormat().toString());
        Log.v(TAG, rawResult.getBarcodeFormat().toString());
        // Do something with the result
        Intent intent=new Intent();
        intent.putExtra("MESSAGE",rawResult.getText() );

        setResult(RESULT_OK,intent);
        finish();
        mScannerView.resumeCameraPreview(this);
    }


}