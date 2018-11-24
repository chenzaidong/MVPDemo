package cn.chenzd.mvpdemo.http.function;

import cn.chenzd.mvpdemo.http.base.BaseResponse;
import cn.chenzd.mvpdemo.http.exception.ServerException;
import io.reactivex.functions.Function;

/**
 * http请求结果转换处理
 *
 * @author czd
 */
public class HttpResponseFunction<T> implements Function<BaseResponse<T>, T> {
    @Override
    public T apply(BaseResponse<T> baseResponse) throws Exception {
        if (!baseResponse.isSuccess()) {
            throw new ServerException(baseResponse.getCode(), baseResponse.getMsg());
        }
        return baseResponse.getData();
    }
}
