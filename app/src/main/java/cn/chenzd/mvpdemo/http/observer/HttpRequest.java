package cn.chenzd.mvpdemo.http.observer;

import com.trello.rxlifecycle2.LifecycleProvider;

import cn.chenzd.mvpdemo.http.base.BaseResponse;
import cn.chenzd.mvpdemo.http.function.HttpExceptionFunction;
import cn.chenzd.mvpdemo.http.function.HttpResponseFunction;
import io.reactivex.Observable;

/**
 * 网络请求
 *
 * @author czd
 */
public class HttpRequest {
    /**
     * 网络请求Observable构建
     * 无管理生命周期
     * 创建在子线程切换到主线程
     *
     * @param observable 被观察者
     * @param <T>        网络请求结果封装类
     * @return 被观察者对象
     */
    public static <T> Observable<T> request(Observable<BaseResponse<T>> observable) {
        return observable.map(new HttpResponseFunction<T>())
                .compose(RxSchedulers.<T>applySchedulers())
                .onErrorResumeNext(new HttpExceptionFunction<T>());
    }

    /**
     * 网络请求Observable构建
     * 绑定管理生命周期
     * 注意subscribeOn切换需要放在绑定生命周期前面
     */
    public static <T,E> Observable<T> request(Observable<BaseResponse<T>> observable, LifecycleProvider<E> lifecycle) {
        if (lifecycle != null) {
            return observable.map(new HttpResponseFunction<T>())
                    .compose(RxSchedulers.<T>applySchedulers())
                    .compose(lifecycle.<T>bindToLifecycle())
                    .onErrorResumeNext(new HttpExceptionFunction<T>());
        }
        return request(observable);
    }

    /**
     * 网络请求Observable构建
     * 绑定管理生命周期
     * 手动管理生命周期,避免内存溢出
     * 注意subscribeOn切换需要放在绑定生命周期前面
     */
    public static <T,E> Observable<T> request(Observable<BaseResponse<T>> observable, LifecycleProvider<E> lifecycle,E event) {
        if (lifecycle != null) {
            return observable.map(new HttpResponseFunction<T>())
                    .compose(RxSchedulers.<T>applySchedulers())
                    .compose(lifecycle.<T>bindUntilEvent(event))
                    .onErrorResumeNext(new HttpExceptionFunction<T>());
        }
        return request(observable);
    }
}
