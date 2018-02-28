package im.goody.android.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.IRepository;


public class InstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseToken";

    @Inject
    IRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();

        App.getDataComponent().inject(this);
    }

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (repository.isSigned())
            repository.sendRegistrationToServer(refreshedToken);
    }

}
