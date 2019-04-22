package com.iris.demo.vungledemoo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vungledemo.R;
import com.vungle.warren.InitCallback;
import com.vungle.warren.LoadAdCallback;
import com.vungle.warren.PlayAdCallback;
import com.vungle.warren.Vungle;
import com.vungle.warren.VungleNativeAd;
import com.vungle.warren.error.VungleException;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = "MainActivity";

    private final String appId="5cbc1d172dd2f50012e029c8";
    private final String interId="MYINTER-8682096";//插屏
    private final String infeedId="INFEED_AD-8729703";//信息流
    private Button mBtLoadInter,mBtShowInsert,mBtShowInfeed;
    private RelativeLayout infeed_container;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSDK();
        mBtLoadInter = findViewById(R.id.button_load_inter);
        mBtShowInsert = findViewById(R.id.button_show_ad);
        Button mBtLoadInfeed = findViewById(R.id.button_load_infeed);
        mBtShowInfeed = findViewById(R.id.button_show_infeed);
        infeed_container= findViewById(R.id.infeed_container);

        mBtLoadInter.setOnClickListener(this);
        mBtShowInsert.setOnClickListener(this);
        mBtLoadInfeed.setOnClickListener(this);
        mBtShowInfeed.setOnClickListener(this);
    }
    private void initSDK(){
        Vungle.init(appId, getApplicationContext(), new InitCallback() {
            @Override
            public void onSuccess() {
                Log.e(TAG,"init__onSuccess");
            }
            @Override
            public void onError(Throwable throwable) {
                Log.e(TAG,"init__onError");
                try {
                    VungleException ex = (VungleException) throwable;
                    if (ex.getExceptionCode() == VungleException.VUNGLE_NOT_INTIALIZED) {
                        initSDK();
                    }
                } catch (ClassCastException cex) {
                    Log.e(TAG, cex.getMessage());
                }
            }
            @Override
            public void onAutoCacheAdAvailable(String s) {
                Log.e(TAG,"init__onAutoCacheAdAvailable");
            }
        });
    }

    private void loadInterstitialAd(final String adId){
        if(Vungle.isInitialized()){
            Toast.makeText(this,"Loading ...",Toast.LENGTH_SHORT).show();
            Vungle.loadAd(adId, new LoadAdCallback() {
                @Override
                public void onAdLoad(String s) {
                    Log.e(TAG,"load__loadInterstitialAd"+s);
                    if (adId.equals(interId)) {
                        mBtShowInsert.setVisibility(View.VISIBLE);
                        mBtLoadInter.setEnabled(false);
                    }else {
                        mBtShowInfeed.setVisibility(View.VISIBLE);
                    }
                }
                @Override
                public void onError(String s, Throwable throwable) {
                    Log.e(TAG,"load__onError"+"---"+throwable.getMessage());
                }
            });
        }else {
            Toast.makeText(this,"Not Inited",Toast.LENGTH_SHORT).show();
        }

    }

    private void showAd(String adId){
        Log.e("Iris","Vungle.canPlayAd"+Vungle.canPlayAd("MYINTER-8682096"));
        if(Vungle.canPlayAd(adId)){
            if(infeedId.equals(adId)){
                VungleNativeAd vungleNativeAd = Vungle.getNativeAd(adId, vunglePlayAdCallback);
                if (vungleNativeAd==null)return;
                View nativeAdView = vungleNativeAd.renderNativeView();
                infeed_container.addView(nativeAdView);
            }else {
                //
                Vungle.playAd(adId, null, vunglePlayAdCallback);
            }
        }
    }

    private final PlayAdCallback vunglePlayAdCallback = new PlayAdCallback() {
        @Override
        public void onAdStart(String s) {
            Log.e(TAG,"load__inFeed_onAdStart"+s);
        }

        @Override
        public void onAdEnd(String s, boolean b, boolean b1) {
            Log.e(TAG,"load__inFeed_onAdEnd"+s);
        }

        @Override
        public void onError(String s, Throwable throwable) {
            Log.e(TAG,"show__infeed_onError"+throwable.getMessage());

        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_load_inter:
                loadInterstitialAd(interId);
                break;
            case R.id.button_show_ad:
                showAd(interId);
                break;
            case R.id.button_load_infeed:
                loadInterstitialAd(infeedId);
                break;
            case R.id.button_show_infeed:
                showAd(infeedId);
                break;
        }
    }
}
