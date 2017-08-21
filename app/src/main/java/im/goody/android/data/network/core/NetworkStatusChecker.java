package im.goody.android.data.network.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import im.goody.android.App;
import io.reactivex.Observable;

public class NetworkStatusChecker {
    static boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) App.getAppContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    static Observable<Boolean> isInternetAvailable() {
        return Observable.just(NetworkStatusChecker.isNetworkAvailable());
    }
}
