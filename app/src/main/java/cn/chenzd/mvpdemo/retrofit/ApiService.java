package cn.chenzd.mvpdemo.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * 网络请求接口
 * @author chenzaidong
 * @date 2017/12/6.
 */

public interface ApiService {
    //http://op.juhe.cn/onebox/movie/video?key=7a856ebb8cf0a19c1c3aee729aa9e74b&q=%E5%BA%B7%E7%86%99%E7%8E%8B%E6%9C%9D
    String BASE_URL ="http://op.juhe.cn/onebox/movie/";
    /**
     * 获取影视信息
     */
    @GET("video")
    Observable<HttpResponse> getTeleplay(@QueryMap Map<String, Object> request);
}
