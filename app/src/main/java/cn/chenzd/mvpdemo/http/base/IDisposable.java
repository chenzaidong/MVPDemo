package cn.chenzd.mvpdemo.http.base;

import io.reactivex.disposables.Disposable;

/**
 * @author czd
 */
public interface IDisposable {
   void addDisposable(Disposable disposable);
   void removeDisposable(Disposable disposable);
   void removeAllDisposable();
}
