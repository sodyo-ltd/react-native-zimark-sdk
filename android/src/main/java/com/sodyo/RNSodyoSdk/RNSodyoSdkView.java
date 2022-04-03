
package com.sodyo.RNSodyoSdk;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.annotations.ReactProp;

import android.util.Log;
import android.widget.FrameLayout;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import javax.annotation.Nullable;

import com.sodyo.sdk.SodyoOpenCvScannerFragment;

public class RNSodyoSdkView extends SimpleViewManager<FrameLayout> {
    static final String TAG = "RNSodyoSdkView";

    static final String TAG_FRAGMENT = "SODYO_SCANNER";

    public static final String REACT_CLASS = "RNSodyoSdkView";

    private final @Nullable ReactApplicationContext mCallerContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNSodyoSdkView(ReactApplicationContext callerContext) {
        mCallerContext = callerContext;
    }

    @Override
    public FrameLayout createViewInstance(ThemedReactContext context) {
        Log.i(TAG, "createViewInstance");

        final FrameLayout view = new FrameLayout(context);
        SodyoOpenCvScannerFragment sodyoFragment = new SodyoOpenCvScannerFragment();

        FragmentManager fragmentManager = mCallerContext.getCurrentActivity().getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }

        fragmentTransaction.add(sodyoFragment, TAG_FRAGMENT);
        fragmentTransaction.commit();

        fragmentManager.executePendingTransactions();

        view.addView(sodyoFragment.getView(), FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        return view;
    }

    @Override
    public void onDropViewInstance(FrameLayout view) {
        super.onDropViewInstance(view);

        Log.i(TAG, "onDropViewInstance");

        try {
          FragmentManager fragmentManager = mCallerContext.getCurrentActivity().getFragmentManager();
          Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT);

          if (fragment != null) {
              fragmentManager.beginTransaction().remove(fragment).commit();
          }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
