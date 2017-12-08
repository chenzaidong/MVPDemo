package cn.chenzd.mvpdemo.http.listener;

/**
 * 请求监听接口
 * @author chenzaidong
 * @date 2017/12/7.
 */

public interface HttpRequestListener {
    /**
     * 取消请求
     */
    void cancel();
}
