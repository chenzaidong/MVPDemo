package cn.chenzd.mvpdemo.http.base;

import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import cn.chenzd.mvpdemo.http.listener.LifeCycleListener;
import cn.chenzd.mvpdemo.http.manager.ActivityStackManager;

/**
 * 基类Activity
 * 所有的Activity都继承该Activity
 *
 * @author czd
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    public LifeCycleListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        ActivityStackManager.getInstance().push(this);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        init(savedInstanceState);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mListener != null) {
            mListener.onDestroy();
        }
        ActivityStackManager.getInstance().remove(this);
    }

    /**
     * 初始化
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 获取资源文件ID
     */
    protected abstract int getContentViewId();


    /**
     * 设置生命监听
     */
    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }
}
