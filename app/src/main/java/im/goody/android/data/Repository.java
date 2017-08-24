package im.goody.android.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.data.network.RestService;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.di.components.DataComponent;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    public Observable<String> register(RegisterReq data) {
        return Observable.just("Completed")
                .delay(2, TimeUnit.SECONDS)
                // TODO: 23.08.2017 Добавить сохранение данных пользователя
                //.doOnNext()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> login(LoginReq data) {
        // TODO: 23.08.2017 потом убрать проверку
        if (data.getEmail().equals(TEST_EMAIL) && data.getPassword().equals(TEST_PASSWORD)) {
            return Observable.just("Completed")
                    .delay(2, TimeUnit.SECONDS)
                    // TODO: 23.08.2017 Сделать нормальное сохранение токена пользователя
                    .doOnNext(s -> preferencesManager.saveUserToken(s))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Observable.error(new Throwable("Invalid cridentials"));
        }
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
    public Observable<List<Deal>> getNews() {
        int count = 100;
        return Observable.range(1, count)
                .subscribeOn(Schedulers.io())
                .flatMap(index -> Observable.just(Deal.getFake()))
                .toList()
                .toObservable()
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    //endregion








}
