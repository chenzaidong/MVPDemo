package cn.chenzd.mvpdemo.http.exception;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import retrofit2.HttpException;

/**
 * @author czd
 */
public class ExceptionHandler {
    /**
     * 未知错误
     */
    public static final int ERROR_UN_KNOWN = 1001;
    /**
     * 解析异常
     */
    public static final int ERROR_ANALYTIC_SERVER_DATA = 1002;
    /**
     * 连接异常
     */
    public static final int ERROR_CONNECT = 1003;
    /**
     * 超时
     */
    public static final int ERROR_TIME_OUT = 1004;

    public static ApiException handlerException(Throwable t) {
        ApiException ex = new ApiException(t);
        if (t instanceof HttpException) {
            ex.setCode(((HttpException) t).code());
            ex.setMsg(t.getMessage());
        } else if (t instanceof ServerException) {
            ex.setCode(((ServerException) t).getCode());
            ex.setMsg(t.getMessage());
        }else if ((t instanceof JsonParseException)
                ||(t instanceof JSONException)
                || (t instanceof ParseException)
                || (t instanceof MalformedJsonException)){
            ex.setMsg("解析错误");
            ex.setCode(ERROR_ANALYTIC_SERVER_DATA);
        }else if (t instanceof ConnectException){
            ex.setCode(ERROR_CONNECT);
            ex.setMsg("连接失败");
        }else if (t instanceof SocketTimeoutException){
            ex.setCode(ERROR_TIME_OUT);
            ex.setMsg("网络超时");
        }else {
            ex.setCode(ERROR_UN_KNOWN);
            ex.setMsg("未知异常");
        }
        return ex;
    }
}
