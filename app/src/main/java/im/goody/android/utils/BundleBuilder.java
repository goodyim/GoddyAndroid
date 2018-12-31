package im.goody.android.utils;

import android.os.Bundle;
import android.os.Parcelable;

@SuppressWarnings("unused")
public class BundleBuilder {

    private final Bundle bundle;

    public BundleBuilder(Bundle bundle) {
        this.bundle = bundle;
    }

    public BundleBuilder() {
        bundle = new Bundle();
    }

    public BundleBuilder putBoolean(String key, boolean value) {
        bundle.putBoolean(key, value);
        return this;
    }

    public BundleBuilder putDouble(String key, double value) {
        bundle.putDouble(key, value);
        return this;
    }


    public BundleBuilder putString(String key, String value) {
        bundle.putString(key, value);
        return this;
    }
    public BundleBuilder putInt(String key, int value) {
        bundle.putInt(key, value);
        return this;
    }

    public BundleBuilder putLong(String key, long value) {
        bundle.putLong(key, value);
        return this;
    }

    public BundleBuilder putParcelable(String key, Parcelable value) {
        bundle.putParcelable(key, value);
        return this;
    }

    public Bundle build() {
        return bundle;
    }

}
