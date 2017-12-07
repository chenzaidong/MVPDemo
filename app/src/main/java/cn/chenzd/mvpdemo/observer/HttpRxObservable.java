package cn.chenzd.mvpdemo.observer;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import cn.chenzd.mvpdemo.function.HttpResultFunction;
import cn.chenzd.mvpdemo.function.ServerResultFunction;
import cn.chenzd.mvpdemo.retrofit.HttpResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 使用Retrofit网络请求Observable(被观察者)
 *
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class HttpRxObservable {

    /**
     * 获取被观察者
     * 网络请求Observable构建
     * 无管理生命周期,容易导致内存溢出
     *
     * @param apiObservable 网络请求参数
     * @return 被观察者对象
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable) {
        return apiObservable.map(new ServerResultFunction())
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取被观察者
     * 网络请求Observable构建
     * 需要继承RxActivity.../RxFragment...
     *
     * @param apiObservable 网络请求参数
     * @param lifecycle     自动管理生命周期,避免内存溢出
     * @return 被观察者对象
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider lifecycle) {
        Observable observable;

        if (lifecycle != null) {
            observable = apiObservable.map(new ServerResultFunction())
                    .compose(lifecycle.bindToLifecycle())
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

    /**
     * 获取被观察者
     * 网络请求Observable构建
     * 传入LifecycleProvider<ActivityEvent>手动管理生命周期,避免内存溢出
     * 需要继承RxActivity,RxAppCompatActivity,RxFragmentActivity
     * @param apiObservable 网络请求参数
     * @param lifecycle 生命周期管理
     * @param event 周期事件
     * @return 被观察者对象
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider<ActivityEvent> lifecycle, ActivityEvent event) {
        Observable observable;
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:ActivityEvent.STOP
            observable = apiObservable.map(new ServerResultFunction())
                    .compose(lifecycle.bindUntilEvent(event))
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

    /**
     * 获取被监听者
     * 备注:网络请求Observable构建
     * data:网络请求参数
     * <h1>补充说明</h1>
     * 传入LifecycleProvider<FragmentEvent>手动管理生命周期,避免内存溢出
     * 备注:需要继承RxFragment,RxDialogFragment
     */
    public static Observable getObservable(Observable<HttpResponse> apiObservable, LifecycleProvider<FragmentEvent> lifecycle, FragmentEvent event) {
        Observable observable;
        if (lifecycle != null) {
            //手动管理移除监听生命周期.eg:FragmentEvent.STOP
            observable = apiObservable
                    .map(new ServerResultFunction())
                    .compose(lifecycle.bindUntilEvent(event))
                    .onErrorResumeNext(new HttpResultFunction<>())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = getObservable(apiObservable);
        }
        return observable;
    }

}
