package cn.chenzd.mvpdemo.http.base;

import java.lang.ref.WeakReference;

import cn.chenzd.mvpdemo.http.listener.LifeCycleListener;

/**
 * 基类Presenter
 * V表示View层接口实例，T 表示activity或者fragment,用于绑定生命周期
 * @author czd
 */
public abstract class BasePresenter<V extends IView,T> implements LifeCycleListener {
    protected WeakReference<V> mViewRef;
    protected WeakReference<T> mActivityRef;

    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 关联view
     */
    private void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }


    /**
     * 关联 activity
     */
    private void attachActivity(T activity) {
        mActivityRef = new WeakReference<>(activity);
    }


    /**
     * 设置生命周期监听
     *
     * @author ZhongDaFeng
     */
    private void setListener(T activity) {
        if (getActivityOrFragment() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivityOrFragment()).setOnLifeCycleListener(this);
            } else if (activity instanceof BaseFragment) {
                ((BaseFragment) getActivityOrFragment()).setOnLifeCycleListener(this);
            }
        }
    }
    /**
     * 获取view
     *
     */
    public V getView() {
        if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();
    }
    /**
     * 获取activity
     *
     */
    public T getActivityOrFragment() {
        if (mActivityRef == null) {
            return null;
        }
        return mActivityRef.get();
    }


    /**
     * 是否已经关联view
     *
     */
    private boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 是否已经关联activity
     *
     */
    private boolean isActivityAttached() {
        return mActivityRef != null && mActivityRef.get() != null;
    }

    /**
     * 销毁
     */
    private void detachView() {
        if (isViewAttached()) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 销毁
     */
    private void detachActivity() {
        if (isActivityAttached()) {
            mActivityRef.clear();
            mActivityRef = null;
        }
    }

    @Override
    public void onDestroy() {
        detachView();
        detachActivity();
    }
}
