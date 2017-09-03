package im.goody.android.data;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.lang.annotation.Annotation;
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
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.HttpException;
import retrofit2.Retrofit;

// TODO replace fake data with api request
public class Repository implements IRepository {

    //region ================= MockData =================

    private static final String TEST_EMAIL = "test@test.com";
    private static final String TEST_PASSWORD = "1234567";

    //endregion

    @Inject
    PreferencesManager preferencesManager;
    @Inject
    RestService restService;
    @Inject
    Retrofit retrofit;

    public Repository() {
        DataComponent component = App.getDataComponent();
        if (component != null)
            component.inject(this);
    }

    //region ================= User =================

    @Override
    public Observable<UserRes> register(RegisterReq data) {
        return restService.registerUser(data)
                .doOnNext(userRes -> {
                    preferencesManager.saveUserToken(userRes.getToken());
                    preferencesManager.saveUserId(userRes.getId());
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserRes> login(LoginReq data) {
        return restService.loginUser(data.getName(), data.getPassword())
                .doOnNext(userRes -> {
                    preferencesManager.saveUserToken(userRes.getToken());
                    preferencesManager.saveUserId(userRes.getId());
                })
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

    @Override
    public <T> T getError(Throwable t, Class<T> tClass) {
        try {
            ResponseBody body = ((HttpException) t).response().errorBody();
            Converter<ResponseBody, T> errorConverter =
                    retrofit.responseBodyConverter(tClass, new Annotation[0]);

            return errorConverter.convert(body);
        } catch (Exception e) {
            return null;
        }
    }

    //endregion

    //region ================= News =================

    @Override
    public Observable<List<Deal>> getNews(int page) {
        return restService.getDeals(preferencesManager.getUserToken(), page)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> createPost(NewPostReq body, Uri uri) {
        return Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .map(this::getPartFromUri)
                .flatMap(part -> restService.uploadDeal(
                        preferencesManager.getUserToken(),
                        RequestBody.create(MultipartBody.FORM, body.getDescription()),
                        body.getCategory(),
                        body.isSubscribersOnly(),
                        part
                ))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> sendReport(long id) {
        return Observable.just("Submitted")
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    //endregion


    private MultipartBody.Part getPartFromUri(Uri uri) {
        if (uri == null) return null;

        String path;
        Cursor cursor = App.getAppContext().
                getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            path = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);
            cursor.close();
        }

        File file = new File(path);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        return MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
    }
}
