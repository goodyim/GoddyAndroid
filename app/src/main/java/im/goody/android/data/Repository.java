package im.goody.android.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.data.network.RestService;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.UserRes;
import im.goody.android.di.components.DataComponent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

// TODO replace fake data with api request
public class Repository implements IRepository{

    //region ================= MockData =================

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "1234567";

    //endregion

    @Inject
    PreferencesManager preferencesManager;
    @Inject
    RestService restService;

    public Repository() {
        DataComponent component = App.getDataComponent();
        if (component != null)
            component.inject(this);
    }

    //region ================= User =================

    @Override
    public Observable<UserRes> register(RegisterReq data) {
        return restService.registerUser(data)
                .doOnNext(userRes ->
                        preferencesManager.saveUserToken(userRes.getToken())
                )
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserRes> login(LoginReq data) {
            return restService.loginUser(data)
                    .doOnNext(userRes -> preferencesManager.saveUserToken(userRes.getToken()))
                    .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean isSigned() {
        return preferencesManager.isTokenPresent();
    }

    @Override
    public boolean isFirstLaunch() {
        return preferencesManager.isFirstStart();
    }

    @Override
    public void firstLaunched() {
        preferencesManager.saveFirstLaunched();
    }

    //endregion

    //region ================= News =================

    @Override
    public Observable<List<Deal>> getNews(int page) {
        return restService.getDeals(preferencesManager.getUserToken(), page)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> createPost(NewPostReq body) {
        return Observable.just("Created")
                .delay(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> sendReport(long id) {
        return Observable.just("Submitted")
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    //endregion








}
