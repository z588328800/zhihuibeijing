package priv.zl.zhihuibeijing.utils;

import android.app.Activity;

import java.util.Stack;

/**
 * Activity管理器
 */

public class ActivityManager {
    private Stack<Activity> activityStack = new Stack<>();
    public static ActivityManager activityManager;

    private ActivityManager() {

    }

    /**
     * 获取activity管理器
     *
     * @return
     */
    public static ActivityManager get() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }

        return activityManager;
    }

    /**
     * activity
     * @param activity
     */
    public void removeActivity(Activity activity) {
        for (int i = 0; i < activityStack.size(); i++) {
            Activity activity1 = activityStack.get(i);
            if (activity1.getClass().equals(activity.getClass())) {
                activityStack.remove(activity1);
                break;
            }
        }
    }

    /**
     * 添加一个activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }


    /**
     * 移除当前显示的activity
     */
    public void removeCurrent() {
        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    /**
     * 移除所有activity
     */
    public void removeAll() {
        for (int i = activityStack.size() - 1; i >= 0; i--) { //从栈顶遍历到栈底
            Activity activity = activityStack.get(i);
            activity.finish();
            activityStack.remove(activity);
        }

        Activity activity = activityStack.lastElement();
        activity.finish();
        activityStack.remove(activity);
    }

    /**
     * 获取栈内Activity数量
     * @return
     */
    public int getSize() {
        return activityStack.size();
    }
}
