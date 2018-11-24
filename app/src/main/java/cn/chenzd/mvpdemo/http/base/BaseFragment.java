package cn.chenzd.mvpdemo.http.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.chenzd.mvpdemo.http.listener.LifeCycleListener;


/**
 * 基类Fragment
 * 所有的Fragment都继承自此Fragment
 * @author czd
 */

public abstract class BaseFragment extends RxFragment {
    protected Unbinder unBinder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mListener != null) {
            mListener.onCreate(savedInstanceState);
        }
        View  rootView = inflater.inflate(getContentViewId(),container,false);
        unBinder = ButterKnife.bind(this,rootView);
        init(savedInstanceState);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null) {
            mListener.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mListener != null) {
            mListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null) {
            mListener.onDestroy();
        }
        //移除view绑定
        if (unBinder != null) {
            unBinder.unbind();
        }
    }

    /**
     * 获取显示view的xml文件ID
     */
    protected abstract int getContentViewId();

    /**
     * 初始化应用程序，设置一些初始化数据,获取数据等操作
     */
    protected abstract void init(Bundle savedInstanceState);


    public LifeCycleListener mListener;
    /**
     * 绑定生命周期监听
     */
    public void setOnLifeCycleListener(LifeCycleListener listener) {
        mListener = listener;
    }

}