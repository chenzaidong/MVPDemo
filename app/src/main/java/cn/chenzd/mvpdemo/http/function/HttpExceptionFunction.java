package cn.chenzd.mvpdemo.http.function;

import cn.chenzd.mvpdemo.http.exception.ExceptionHandler;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * http异常处理转换处理
 * @author czd
 */
public class HttpExceptionFunction<T> implements Function<Throwable,Observable<T>> {

    @Override
    public Observable<T> apply(Throwable throwable) throws Exception {
        return Observable.error(ExceptionHandler.handlerException(throwable));
    }
}
