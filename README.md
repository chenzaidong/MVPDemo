[原文地址](http://blog.csdn.net/u014702653/article/details/75268919)

## 错误处理
在常见的网络请求框架中一般会有两个回调函数

```java
 /**
     * 错误/异常回调
     */
    protected abstract void onError(ApiException e);

    /**
     * 成功回调
     */
    protected abstract void onSuccess(T response);

```

定义`onError`回调函数触发的场景是：1.`异常`  2.`错误`
1. 异常：请求异常，解析数据出错，网络异常等等
2. 错误：某一次请求逻辑错误，（例如：登录错误）

将上述两种情况交给`onError`回调函数处理
在请求逻辑成功的时候触发一个`onSuccess`函数。
这样监听者就只需要两个函数，一个失败，一个成功，失败提示给用户，成功负责展示数据，跳转页面等

下面看到`map` 处理逻辑,`onErrorResumeNext`处理异常，

```java
 Observable observable = apiObservable
                .map(new ServerResultFunction())
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
```


定义一个异常处理类`ExceptionEngine`

```java
public class ExceptionEngine {

    public static final int UN_KNOWN_ERROR = 1000;//未知错误
    public static final int ANALYTIC_SERVER_DATA_ERROR = 1001;//解析(服务器)数据错误
    public static final int ANALYTIC_CLIENT_DATA_ERROR = 1002;//解析(客户端)数据错误
    public static final int CONNECT_ERROR = 1003;//网络连接错误
    public static final int TIME_OUT_ERROR = 1004;//网络连接超时

    public static ApiException handleException(Throwable e) {
        ApiException ex;
        if (e instanceof HttpException) {             //HTTP错误
            HttpException httpExc = (HttpException) e;
            ex = new ApiException(e, httpExc.code());
            ex.setMsg("网络错误");  //均视为网络错误
            return ex;
        } else if (e instanceof ServerException) {    //服务器返回的错误
            ServerException serverExc = (ServerException) e;
            ex = new ApiException(serverExc, serverExc.getCode());
            ex.setMsg(serverExc.getMsg());
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {  //解析数据错误
            ex = new ApiException(e, ANALYTIC_SERVER_DATA_ERROR);
            ex.setMsg("解析错误");
            return ex;
        } else if (e instanceof ConnectException) {//连接网络错误
            ex = new ApiException(e, CONNECT_ERROR);
            ex.setMsg("连接失败");
            return ex;
        } else if (e instanceof SocketTimeoutException) {//网络超时
            ex = new ApiException(e, TIME_OUT_ERROR);
            ex.setMsg("网络超时");
            return ex;
        } else {  //未知错误
            ex = new ApiException(e, UN_KNOWN_ERROR);
            ex.setMsg("未知错误");
            return ex;
        }
    }
}
```

异常处理类中，都是常见的错误类型，我们通过解析`Throwable`转换成统一的错误类`ApiException`

```java
public class ApiException extends Exception {
    private int code;//错误码
    private String msg;//错误信息
    ......
 }
```

还有一个自定义错误类型类 `ServerException`,一般我们开发中都会跟服务器约定一种接口请求返回的数据。比如：
* int code：表示接口请求状态，0表示成功，-101表示密码错误等等
* String msg：表示接口请求返回的描述。success，”token过期”等等
* Object result：成功是返回的数据

那么我们就可以在解析服务端返回数据的时候，当code!=200，就抛出ServerException，表示逻辑错误


到这里我们常见的错误类型，异常等都处理完了，
通过`ExceptionEngine`转化为统一的错误类型`ApiException`，
在订阅者回调`onError(ApiException e)`
就可以很方便知道错误的状态码以及对应的描述信息。

```java
Observable observable = apiObservable
                .map(new ServerResultFunction())
                .onErrorResumeNext(new HttpResultFunction<>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
```


我们使用`onErrorResumeNext(new HttpResultFunction<>())`操作符对`Retrofit`
网络请求抛出的`Exception`进行处理，我们定义`HttpResultFunction`处理`Retrofit`抛出的`Exception`，
通过`ExceptionEngine`转化为统一的错误类型`ApiException`

```java
public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable) throws Exception {
        //打印具体错误
        LogUtils.e("HttpResultFunction:" + throwable);
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
```

这里`Result`我们使用`Object`因为接口时通用的，
服务端返回的接口类型也是多样的，可能是`列表`，也可能是`JSON`对象，
或者`String`字符串，所以这里我们使用`Object`，
在数据解析的时候在转化成为具体的类型


## 管理生命周期

RxJava使用订阅模式，那我们就需要封装一个被订阅者，一个订阅者，以及使用RxLifecycle自动管理订阅的生命周期

根据Retrofit 和 RxJava，先完成2步
1. 创建apiServer接口，定义所有请求接口
2. 构建Retrofit实例，配置参数，单例模式

下面进行封装：
1. 构建网络请求,被观察者对象
2. 构建订阅者对象

`HttpRxObservable` 被观察者对象
```java

/**
 * 使用Retrofit网络请求Observable(被观察者)
 *
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class HttpRxObservable {

   ...

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
```

`HttpRxObserver`观察者对象

```java

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
```

使用网络请求的过程中我们肯定会遇到取消请求的场景，
这里我们实现一个`HttpRequestListener`，
为每一个请求添加唯一的TAG用来标识具体的每一个请求，
开始请求时保存TAG，请求成功/失败移除标志，
同时TAG也用做取消请求的标志。

### 在MVP中使用RxLifecycle 管理订阅生命周期

定义一个Presenter基类

```java

/**
 * 基类Presenter
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class BasePresenter<V, T> implements LifeCycleListener {

    protected Reference<V> mViewRef;
    protected V mView;
    protected Reference<T> mActivityRef;
    protected T mActivity;


    public BasePresenter(V view, T activity) {
        attachView(view);
        attachActivity(activity);
        setListener(activity);
    }

    /**
     * 设置生命周期监听
     *
     * @author ZhongDaFeng
     */
    private void setListener(T activity) {
        if (getActivity() != null) {
            if (activity instanceof BaseActivity) {
                ((BaseActivity) getActivity()).setOnLifeCycleListener(this);
            } else if (activity instanceof BaseFragment) {
                ((BaseFragment) getActivity()).setOnLifeCycleListener(this);
            }
        }
    }

    /**
     * 关联
     *
     * @param view
     */
    private void attachView(V view) {
        mViewRef = new WeakReference<V>(view);
        mView = mViewRef.get();
    }

    /**
     * 关联
     *
     * @param activity
     */
    private void attachActivity(T activity) {
        mActivityRef = new WeakReference<T>(activity);
        mActivity = mActivityRef.get();
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

    /**
     * 获取
     *
     * @return
     */
    public V getView() {
        if (mViewRef == null) {
            return null;
        }
        return mViewRef.get();
    }

    /**
     * 获取
     *
     * @return
     */
    public T getActivity() {
        if (mActivityRef == null) {
            return null;
        }
        return mActivityRef.get();
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 是否已经关联
     *
     * @return
     */
    public boolean isActivityAttached() {
        return mActivityRef != null && mActivityRef.get() != null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        detachView();
        detachActivity();
    }
}

```

JAVA弱引用，管理View的引用，以及activity/Fragment的引用，
避免强引用导致资源无法释放而造成的内存溢出

















