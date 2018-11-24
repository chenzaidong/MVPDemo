package cn.chenzd.mvpdemo.http.base;

/**
 * http请求结果接口
 * @author czd
 */
public interface BaseResponse<T> {
    /**
     * 是否成功
     * @return true表示成功，否则表示失败
     */
    boolean isSuccess();

    /**
     * 获取消息码
     * @return code
     */
    int getCode();

    /**
     * 获取消息信息
     * @return msg
     */
    String getMsg();

    T getData();
}
