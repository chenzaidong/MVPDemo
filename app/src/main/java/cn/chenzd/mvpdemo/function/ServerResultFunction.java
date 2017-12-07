package cn.chenzd.mvpdemo.function;


import com.google.gson.Gson;

import cn.chenzd.mvpdemo.exception.ServerException;
import cn.chenzd.mvpdemo.retrofit.HttpResponse;
import io.reactivex.functions.Function;

/**
 * http请求结果处理
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class ServerResultFunction implements Function<HttpResponse,Object> {
    @Override
    public Object apply(HttpResponse httpResponse) throws Exception {
        if (!httpResponse.isSuccess()){
            throw new ServerException(httpResponse.getCode(),httpResponse.getMsg());
        }
        return new Gson().toJson(httpResponse.getResult());
    }
}
