package cn.chenzd.mvpdemo.http.base;

import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import cn.chenzd.mvpdemo.http.listener.LifeCycleListener;
import cn.chenzd.mvpdemo.http.manager.ActivityStackManager;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 基类Activity
 * 所有的Activity都继承该Activity
 *
 * @author czd
 */
public abstract class BaseActivity extends RxAppCompatActivity implements IDisposable{
    public LifeCycleListener mListener;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
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
        removeAllDisposable();
    }

    @Override
    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void removeDisposable(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        mCompositeDisposable.remove(disposable);
    }

    @Override
    public void removeAllDisposable() {
        mCompositeDisposable.clear();
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
