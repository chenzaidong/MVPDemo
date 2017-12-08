package cn.chenzd.mvpdemo.http.retrofit;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import cn.chenzd.mvpdemo.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit工厂类，获取Retrofit对象，对Retrofit进行配置
 *
 * @author chenzaidong
 * @date 2017/12/6.
 */

public class RetrofitFactory {
    private static final int TIME_OUT = 4;

    private ApiService mApiService;

    private static class SingleInstance {
        private static final RetrofitFactory INSTANCE = new RetrofitFactory();
    }

    public static RetrofitFactory getInstance() {
        return SingleInstance.INSTANCE;
    }

    private RetrofitFactory() {
        Retrofit mRetrofit;
        //配置网络请求客户端
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        //如果是Debug版本 则添加日志查看器
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor((message -> {
                Log.d("okHttp", message);
            }));
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
        //配置网络请求
        mRetrofit = new Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return mApiService;
    }
}
