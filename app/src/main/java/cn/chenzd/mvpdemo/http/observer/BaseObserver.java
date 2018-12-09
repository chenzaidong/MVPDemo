package cn.chenzd.mvpdemo.http.observer;


import cn.chenzd.mvpdemo.http.exception.ApiException;
import cn.chenzd.mvpdemo.http.exception.ExceptionHandler;
import io.reactivex.observers.DefaultObserver;

/**
 * @author czd
 */
public abstract class BaseObserver<T> extends DefaultObserver<T> {
    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            onError((ApiException) e);
        } else {
            onError(new ApiException(e,ExceptionHandler.ERROR_UN_KNOWN, "未知异常"));
        }
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    public abstract void onError(ApiException e);



    @Override
    public void onComplete() {
    }
}
