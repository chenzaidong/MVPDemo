package cn.chenzd.mvpdemo.function;


import cn.chenzd.mvpdemo.exception.ExceptionEngine;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * http异常结果转换处理
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class HttpResultFunction<T> implements Function<Throwable,Observable<T>> {
    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
