package cn.chenzd.mvpdemo.http.base;

import android.support.annotation.Nullable;

/**
 * View层抽象接口
 * @author czd
 */
public interface IView {
    /**
     * 开始加载
     */
    void onLoading();

    /**
     * 加载完成
     * @param msg 完成提示
     */
    void onSuccess(@Nullable String msg);

    /**
     * 加载错误
     * @param msg 错误提示
     */
    void onError(String msg);
}
