package okhttp.custom.android.getcontacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

public class ActivityUtil {

    public static boolean isFinishing(Activity activity) {
        return (activity == null || activity.isFinishing());
    }

    public static void startActivity(Activity activity, Class targetClass) {
        Intent intent = new Intent(activity, targetClass);
        activity.startActivity(intent);
    }

    public static void runOnUiThread(Runnable runnable) {
        Looper mainLooper = Looper.getMainLooper();
        new Handler(mainLooper).post(runnable);
    }

}
