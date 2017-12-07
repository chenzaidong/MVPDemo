package cn.chenzd.mvpdemo.listener;

import android.os.Bundle;

/**
 * 生命周期监听
 * @author chenzaidong
 * @date 2017/12/7.
 */

public interface LifeCycleListener {

    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

}