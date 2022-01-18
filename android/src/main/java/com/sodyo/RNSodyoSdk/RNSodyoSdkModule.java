package com.sodyo.RNSodyoSDK;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.Promise;

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import android.util.Log;
import android.content.Intent;
import android.app.Application;
import android.app.Activity;
import android.graphics.Color;

import org.json.JSONObject;

import com.sodyo.sdk.Sodyo;
import com.sodyo.sdk.SodyoInitCallback;
import com.sodyo.sdk.SodyoScannerActivity;
import com.sodyo.sdk.SodyoScannerCallback;
import com.sodyo.sdk.ScanResultData;

import com.sodyo.sdk.ScanType;
import static com.sodyo.sdk.ScanType.BARCODE;
import static com.sodyo.sdk.ScanType.QR_CODE;
import static com.sodyo.sdk.ScanType.SODYO_MARKER;

import com.google.gson.Gson;

public class RNSodyoSdkModule extends ReactContextBaseJavaModule {
  private static final int SODYO_SCANNER_REQUEST_CODE = 2222;

  private static final String TAG = "SodyoSDK";

  private Gson resultsGson = new Gson();

  private final ReactApplicationContext reactContext;

  private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
   @Override
      public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
        Log.i(TAG, "onActivityResult()");

        if (requestCode == SODYO_SCANNER_REQUEST_CODE) {
          sendEvent("EventCloseSodyoScanner", null);
        }
      }
  };

  public RNSodyoSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.reactContext.addActivityEventListener(mActivityEventListener);
  }

  @Override
  public String getName() {
    return "RNSodyoSdk";
  }

  private class SodyoCallback implements SodyoScannerCallback, SodyoInitCallback {

      private Callback successCallback;
      private Callback errorCallback;
      private boolean isCallbackUsed;

      public SodyoCallback(Callback successCallback, Callback errorCallback) {
          this.successCallback = successCallback;
          this.errorCallback = errorCallback;
      }

      /**
       * SodyoInitCallback implementation
       */
      public void onSodyoAppLoadSuccess() {
          String message = "onSodyoAppLoadSuccess";
          Log.i(TAG, message);

          if (this.successCallback == null || this.isCallbackUsed) {
            return;
          }

          this.successCallback.invoke();
          this.isCallbackUsed = true;

          SodyoCallback callbackClosure = new SodyoCallback(null, null);
          Sodyo.getInstance().setScannerCallback(callbackClosure);
      }

      /**
       * SodyoInitCallback implementation
       */
      public void onSodyoAppLoadFailed(String error) {
          String message = "onSodyoAppLoadFailed. Error=\"" + error + "\"";
          Log.e(TAG, message);

          if (this.errorCallback == null || this.isCallbackUsed) {
            return;
          }

          this.errorCallback.invoke(error);
          this.isCallbackUsed = true;
      }

      /**
       * SodyoInitCallback implementation
       */
      @Override
      public void sodyoError(Error err) {
          String message = "sodyoError. Error=\"" + err + "\"";
          Log.e(TAG, message);

          WritableMap params = Arguments.createMap();
          params.putString("error", err.getMessage());
          sendEvent("EventSodyoError", params);
      }


      @Override
      public void onResult(ScanResultData scanResultData) {
         Log.i(TAG, "onResult()");

         WritableMap params = Arguments.createMap();

         if (scanResultData == null) {
           params.putString("data", "{}");
           sendEvent("EventContent", params);
         } else {
			     String jsonStr = resultsGson.toJson(scanResultData.getScanResults());

           Log.i(TAG, "getScanResults " + jsonStr);

           params.putString("data", jsonStr);

           sendEvent("EventContent", params);
         }
      }
  }

  @ReactMethod
  public void init(Callback successCallback, Callback errorCallback) {
      Log.i(TAG, "init()");

      if (Sodyo.isInitialized()) {
          Log.i(TAG, "init(): already initialized, ignore");
          return;
      }

      final SodyoCallback callbackClosure = new SodyoCallback(successCallback, errorCallback);

      UiThreadUtil.runOnUiThread(new Runnable() {
          @Override
          public void run() {
              Sodyo.init(
                      (Application) reactContext.getApplicationContext(),
                      callbackClosure
              );
          }
      });
  }



  @ReactMethod
  public void start() {
      Log.i(TAG, "start()");
      Intent intent = new Intent(this.reactContext, SodyoScannerActivity.class);
      Activity activity = getCurrentActivity();
      activity.startActivityForResult(intent, SODYO_SCANNER_REQUEST_CODE);
  }

  @ReactMethod
  public void close() {
      Log.i(TAG, "close()");
      Activity activity = getCurrentActivity();
      activity.finishActivity(SODYO_SCANNER_REQUEST_CODE);
  }

  @ReactMethod
  public void setUserInfo(ReadableMap userInfo) {
      Log.i(TAG, "setUserInfo()");

      if(userInfo != null) {
        Sodyo.getInstance().setUserInfo(ConversionUtil.toMap(userInfo));
      }
  }

  @ReactMethod
  public void setCustomAdLabel(String label) {
      Log.i(TAG, "setCustomAdLabel()");
      Sodyo.setCustomAdLabel(label);
  }

  @ReactMethod
  public void setScannerParams(ReadableMap scannerPreferences) {
      Log.i(TAG, "setScannerParams()");
      Sodyo.setScannerParams(ConversionUtil.toFlatMap(scannerPreferences));
  }

  @ReactMethod
  public void addScannerParam(String key, String value) {
      Log.i(TAG, "addScannerParam()");
      Sodyo.addScannerParams(key, value);
  }

  @ReactMethod
  public void setDynamicProfileValue(String key, String value) {
     Log.i(TAG, "setDynamicProfileValue()");
     Sodyo.setDynamicProfileValue(key, value);
  }

  @ReactMethod
  public void setScanModes(ReadableArray scanTypes) {
     List<Object> typesList = ConversionUtil.toList(scanTypes);
     ScanType[] scanTypesList = new ScanType[typesList.size()];
     int i = 0;

     for (Object o : typesList) {
       scanTypesList[i] = ScanType.valueOf(o.toString());
       i++;
     }

     Log.i(TAG, "setScanModes() " + scanTypesList);

     Sodyo.setScanModes(scanTypesList);
  }

   private void sendEvent(String eventName, @Nullable WritableMap params) {
      this.reactContext
          .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
          .emit(eventName, params);
    }
}
