package cn.chenzd.mvpdemo.http.manager;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity栈管理类
 * 单例
 *
 * @author czd
 */
public class ActivityStackManager {
    private Stack<Activity> mStack;

    private ActivityStackManager() {
        mStack = new Stack<>();
    }

    private static class Single {
        private static final ActivityStackManager INSTANCE = new ActivityStackManager();
    }

    public static ActivityStackManager getInstance() {
        return Single.INSTANCE;
    }

    /**
     * 压栈
     */
    public void push(Activity activity) {
        mStack.push(activity);
    }

    /**
     * 出栈
     */
    public Activity pop() {
        if (mStack.isEmpty()) {
            return null;
        }
        return mStack.pop();
    }

    /**
     * 栈顶
     */
    public Activity peek() {
        if (mStack.isEmpty()) {
            return null;
        }
        return mStack.peek();
    }

    /**
     * 清除所有Activity
     */
    public void cleanActivity() {
        while (!mStack.isEmpty()) {
            mStack.pop();
        }
    }

    /**
     * 移除activity
     */
    public void remove(Activity activity) {
        if (mStack.size() > 0 && activity == mStack.peek()) {
            mStack.pop();
        } else {
            mStack.remove(activity);
        }
    }

    public void finishAllActivity() {
        while (!mStack.isEmpty()) {
            mStack.pop();
        }
    }

    /**
     * 退出应用
     */
    public void exitApp() {
        try {
            finishAllActivity();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
