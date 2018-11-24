package cn.chenzd.mvpdemo.http.exception;

/**
 * 封装网络请求异常处理类
 * 异常分为2类
 * 1.异常[程序异常，网络异常，解析异常等]
 * 2.错误[接口逻辑错误，比如接口返回code=-101,msg="账号密码错误"]
 *
 * @author czd
 */
public class ApiException extends Exception {
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误提示信息
     */
    private String msg;

    public ApiException(Throwable cause, int code, String msg) {
        super(cause);
        this.code = code;
        this.msg = msg;
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ApiException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
