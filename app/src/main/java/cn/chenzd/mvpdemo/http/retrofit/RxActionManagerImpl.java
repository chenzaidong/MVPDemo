package cn.chenzd.mvpdemo.http.retrofit;

import android.util.ArrayMap;

import io.reactivex.disposables.Disposable;

/**
 * RxJavaAction 管理实现类
 * 单例
 *
 * @author chenzaidong
 * @date 2017/12/7.
 */

public class RxActionManagerImpl implements RxActionManager<Object> {
    /**
     * 处理请求列表
     */
    private ArrayMap<Object, Disposable> mMaps;

    private RxActionManagerImpl() {
        mMaps = new ArrayMap<>();
    }

    private static class SingleInstance {
        private static final RxActionManagerImpl INSTANCE = new RxActionManagerImpl();
    }

    public  static RxActionManagerImpl getInstance() {
        return SingleInstance.INSTANCE;
    }

    @Override
    public void add(Object tag, Disposable disposable) {
        if (!mMaps.isEmpty()) {
            mMaps.put(tag, disposable);
        }
    }

    @Override
    public void remove(Object tag) {
        mMaps.remove(tag);
    }

    @Override
    public void cancel(Object tag) {
        if (mMaps.isEmpty()) {
            return;
        }
        if (mMaps.get(tag) == null) {
            return;
        }
        if (!mMaps.get(tag).isDisposed()) {
            mMaps.get(tag).dispose();
        }
        mMaps.remove(tag);
    }

    /**
     * 判断是否已经取消了请求
     *
     * @return
     */
    public boolean isDisposed(Object tag) {
        if (mMaps.isEmpty() || mMaps.get(tag) == null) {
            return true;
        }
        return mMaps.get(tag).isDisposed();
    }
}
