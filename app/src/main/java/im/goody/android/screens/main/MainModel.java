package im.goody.android.screens.main;

import java.util.List;
import java.util.concurrent.TimeUnit;

import im.goody.android.core.BaseModel;
import im.goody.android.data.dto.Deal;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainModel extends BaseModel {
    public Observable<List<Deal>> getNews() {
        //TODO replace with api request
        int count = 100;
        return Observable.range(1, count)
                .subscribeOn(Schedulers.io())
                .flatMap(index -> Observable.just(Deal.getFake()))
                .toList()
                .toObservable()
                .delay(3, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }
}
