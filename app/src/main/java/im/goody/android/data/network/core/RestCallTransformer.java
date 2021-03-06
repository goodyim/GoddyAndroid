package im.goody.android.data.network.core;

import android.support.annotation.Nullable;
import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import im.goody.android.utils.AppConfig;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RestCallTransformer {
    private static final String TAG = "RestCallTransformer";

    public static Map<String, RequestBody> objectToPartMap(Object object, @Nullable String rootElement) {
        try {
            Map<String, RequestBody> result = new HashMap<>();
            Class objClass = object.getClass();
            String format = rootElement + "[%s]";
            String nameSpaceFormat = rootElement + "[%s][%s]";

            if (AppConfig.DEBUG) {
                Log.d(TAG, rootElement);
            }

            for (Field field : objClass.getDeclaredFields()) {
                field.setAccessible(true);

                String name = field.getName();
                if (field.isAnnotationPresent(JsonProperty.class))
                    name = field.getAnnotation(JsonProperty.class).value();

                String key;

                if (field.isAnnotationPresent(NameSpace.class)) {
                    String nameSpace = field.getAnnotation(NameSpace.class).value();
                    key = String.format(nameSpaceFormat, nameSpace, name);
                } else {
                    key = String.format(format, name);
                }

                String value = String.valueOf(field.get(object));

                result.put(
                        key,
                        RequestBody.create(MultipartBody.FORM, value));
                if (AppConfig.DEBUG) {
                    Log.d(TAG, key + ": " + value);
                }
            }

            if (AppConfig.DEBUG) {
                Log.d(TAG, rootElement);
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
