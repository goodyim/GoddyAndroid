package im.goody.android.data;

import java.util.List;
import java.util.concurrent.TimeUnit;

import im.goody.android.data.dto.Auth;
import im.goody.android.data.dto.Deal;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

// TODO replace fake data with api request
public class Repository implements IRepository{
    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "1234567";

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

    @Override
    public Observable<String> login(Auth data) {
        if (data.getEmail().equals(TEST_EMAIL) && data.getPassword().equals(TEST_PASSWORD)) {
            return Observable.just("Completed")
                    .delay(3, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            return Observable.error(new Throwable("Invalid cridentials"));
        }
    }
}
