package im.goody.android.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

import im.goody.android.App;

public class FileUtils {

    public static Uri uriFromFile(File file) {
        Context context = App.getAppContext();

        if (Build.VERSION.SDK_INT > 21) {
            return FileProvider.getUriForFile(context, context.getPackageName()+ ".fileprovider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
