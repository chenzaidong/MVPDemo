package cn.chenzd.mvpdemo.observer;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import cn.chenzd.mvpdemo.exception.ApiException;
import cn.chenzd.mvpdemo.exception.ExceptionEngine;
import cn.chenzd.mvpdemo.listener.HttpRequestListener;
import cn.chenzd.mvpdemo.retrofit.RxActionManagerImpl;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 使用Retrofit网络请求Observer(观察者)
 * 备注：
 * 1、重写onSubscribe,添加请求标识
 * 2、重写onError，封装错误/异常处理，移除请求
 * 3、重写onNext，移除请求
 * 4、重写cancel，取消请求
 *
 * @author chenzaidong
 * @date 2017/12/7.
 */

public abstract class HttpRxObserver<T> implements Observer<T>, HttpRequestListener {
    /**
     * 请求标识
     */
    private String mTag;

    public HttpRxObserver() {
    }

    public HttpRxObserver(String tag) {
        this.mTag = tag;
    }


    @Override
    public void cancel() {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().cancel(mTag);
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().add(mTag, d);
        }

    }

    @Override
    public void onNext(@NonNull T t) {
        if (!TextUtils.isEmpty(mTag)) {
            RxActionManagerImpl.getInstance().remove(mTag);
        }
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        RxActionManagerImpl.getInstance().remove(mTag);
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e, ExceptionEngine.UN_KNOWN_ERROR));
        }
    }

    /**
     * 是否已经处理
     */
    public boolean isDisposed() {
        if (TextUtils.isEmpty(mTag)) {
            return true;
        }
        return RxActionManagerImpl.getInstance().isDisposed(mTag);
    }

    /**
     * 错误/异常回调
     *
     * @param e
     */
    protected abstract void onError(ApiException e);

    /**
     * 成功回调
     *
     * @param response
     */
    protected abstract void onSuccess(T response);

    /**
     * 开始处理
     *
     * @param disposable
     */
    protected abstract void onStart(Disposable disposable);
}
