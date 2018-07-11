package im.goody.android.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import im.goody.android.App;
import im.goody.android.Constants;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Feedback;
import im.goody.android.data.dto.HelpInfo;
import im.goody.android.data.dto.User;
import im.goody.android.data.local.PreferencesManager;
import im.goody.android.data.network.RestService;
import im.goody.android.data.network.core.RestCallTransformer;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.NewCommentReq;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.CommentRes;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.data.network.res.FollowRes;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.data.network.res.UserRes;
import im.goody.android.di.components.DataComponent;
import im.goody.android.utils.AppConfig;
import im.goody.android.utils.FileUtils;
import im.goody.android.utils.TextUtils;
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

public class Repository implements IRepository {
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

        return restService.registerUser(getFcmToken(),
                RestCallTransformer.objectToPartMap(data, "user"))
                .doOnNext(userRes -> {
                    preferencesManager.saveUser(userRes);
                    setProfileFilled(false);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserRes> login(LoginReq data) {
        return restService.loginUser(getFcmToken(), data.getName(), data.getPassword())
                .doOnNext(preferencesManager::saveUser)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public boolean isSigned() {
        return preferencesManager.isTokenPresent();
    }

    @Override
    public boolean isProfileFilled() {
        return preferencesManager.isProfileFilled();
    }

    @Override
    public void setProfileFilled(boolean isFilled) {
        preferencesManager.setProfileFilled(isFilled);
    }

    //endregion

    //region ================= News =================

    @Override
    public Observable<List<Deal>> getPosts(String id, String contentType, int page) {
        if (id.equals(Constants.ID_NONE))
            id = null;

        return restService.getDeals(id, page, contentType)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Deal> getDeal(long id) {
        return restService.getDeal(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> createPost(NewPostReq body, Uri uri) {
        return getPart(uri, "upload")
                .flatMap(part ->
                        restService.uploadDeal(
                                RestCallTransformer.objectToPartMap(body, "good_deal"),
                                part.getPart()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> editPost(Long id, NewPostReq body, Uri imageUri) {
        return getPart(imageUri, "upload")
                .flatMap(part ->
                        restService.updateDeal(
                                id,
                                RestCallTransformer.objectToPartMap(body, "good_deal"),
                                part.getPart()))
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<ResponseBody> createEvent(NewEventReq body, Uri imageUri) {
        return getPart(imageUri, "upload")
                .flatMap(part ->
                        restService.uploadDeal(
                                RestCallTransformer.objectToPartMap(body, "good_deal"),
                                part.getPart()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> editEvent(Long id, NewEventReq body, Uri imageUri) {
        return getPart(imageUri, "upload")
                .flatMap(part ->
                        restService.updateDeal(
                                id,
                                RestCallTransformer.objectToPartMap(body, "good_deal"),
                                part.getPart()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> sendReport(long id) {
        return Observable.just("Submitted")
                .delay(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<User> getUserProfile(String identifier) {
        return restService.getUserProfile(identifier)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<FollowRes> changeFollowState(String id) {
        return restService.changeFollowState(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Deal> likeDeal(long id) {
        return restService.like(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ParticipateRes> changeParticipateState(long id) {
        return restService.participate(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<EventStateRes> changeEventState(long id) {
        return restService.changeEventState(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public UserRes.User getCurrentUser() {
        UserRes.User user = new UserRes.User();

        user.setId(preferencesManager.getUserId());
        user.setAvatarUrl(preferencesManager.getUserAvatarUrl());
        user.setName(preferencesManager.getUserName());

        return user;
    }

    @Override
    public Observable<ResponseBody> logout() {
        return restService.logout()
                .doOnNext(res -> {
                    preferencesManager.clearUserData();
                    preferencesManager.setProfileFilled(true);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<Deal>> getEvents() {
        return getEvents(null, null);
    }

    @Override
    public Observable<List<Deal>> getEvents(String userId, String state) {
        return restService.getEvents(userId, state)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Uri> cacheWebImage(String imageUrl) {
        return Observable.just(imageUrl)
                .subscribeOn(Schedulers.io())
                .map(url -> {
                    Bitmap bmp = Picasso.with(App.getAppContext())
                            .load(imageUrl)
                            .get();
                    File file = cacheBitmap(bmp);

                    return FileUtils.uriFromFile(file);
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> deletePost(long id) {
        return restService.deletePost(id)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> recoverPassword(String email) {
        return restService.recoverPassword(email)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void sendRegistrationToServer(String refreshedToken) {
        restService.sendFcmToken(refreshedToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> Log.d(this.getClass().getName(), "Token sent"),
                        Throwable::printStackTrace);
    }

    @Override
    public Observable<List<Feedback>> getFeedback() {
        return restService.loadNotifications()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<HelpInfo> loadHelpInfo() {
        return restService.getHelpInfo()
                .map(string -> {
                    if (TextUtils.isEmpty(string)) {
                        return new HelpInfo();
                    } else {
                        return new ObjectMapper().readValue(string, HelpInfo.class);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> updateHelpInfo(HelpInfo body) {
        return restService.sendHelpInfo(body)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ResponseBody> deleteComment(long commentId) {
        return restService.deleteComment(commentId);
    }

    @Override
    public Observable<ResponseBody> fillProfile(HelpInfo body) {
        return updateHelpInfo(body)
                .doOnNext(res -> preferencesManager.setProfileFilled(true));
    }

    //endregion

    // ======= region Comments =======

    @Override
    public Observable<CommentRes> sendComment(long dealId, NewCommentReq body) {
        return restService.sendComment(dealId, body)
                .observeOn(AndroidSchedulers.mainThread());
    }

    // endregion


    private File cacheBitmap(Bitmap bmp) throws IOException {
        File file = new File(getCachePath(), Constants.CACHE_FILE_NAME);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bitmapData = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();

        return file;
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

    private String getCachePath() {
        return String.format(AppConfig.CACHE_PATH_FORMAT, Environment.getExternalStorageDirectory());
    }

    private Observable<PartContainer> getPart(Uri uri, String partName) {
        return Observable.just(partName)
                .subscribeOn(Schedulers.io())
                .map(partName1 -> getPartFromUri(uri, partName1));
    }


    private PartContainer getPartFromUri(Uri uri, String partName) throws IOException {
        if (uri == null) return new PartContainer(null);


        InputStream input = App.getAppContext().getContentResolver().openInputStream(uri);
        Bitmap bm = BitmapFactory.decodeStream(input);

        File file = cacheBitmap(bm);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData(partName,
                System.currentTimeMillis() + file.getName(), reqFile);

        return new PartContainer(part);
    }

    private String getFcmToken() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    private class PartContainer {
        private final MultipartBody.Part part;

        private PartContainer(MultipartBody.Part part) {
            this.part = part;
        }

        private MultipartBody.Part getPart() {
            return part;
        }
    }
}
