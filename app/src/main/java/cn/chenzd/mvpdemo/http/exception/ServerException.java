package cn.chenzd.mvpdemo.http.exception;

/**
 * 自定义服务器异常
 * 比如code=-101
 * @author czd
 */
public class ServerException extends RuntimeException {
    public ServerException() {
    }

    public ServerException(int code, String msg) {
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

    /**
     * 错误码
     */

    private int code;
    /**
     * 错误提示信息
     */
    private String msg;

    @Override
    public String toString() {
        return "ServerException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
