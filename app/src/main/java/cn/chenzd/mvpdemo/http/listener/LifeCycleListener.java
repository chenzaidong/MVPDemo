package cn.chenzd.mvpdemo.http.listener;

import android.os.Bundle;

/**
 * 生命周期监听
 * 使得mvp架构中，p层和view层的实现类生命周期一致
 * @author czd
 */
public interface LifeCycleListener {
    void onCreate(Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();
}
