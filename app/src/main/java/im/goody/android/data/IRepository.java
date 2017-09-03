package im.goody.android.data;

import android.net.Uri;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface IRepository {

    Observable<UserRes> login(LoginReq data);
    Observable<UserRes> register(RegisterReq data);
    boolean isSigned();

    boolean isFirstLaunch();
    void firstLaunched();
    <T> T getError(Throwable t, Class<T> tClass);

    Observable<List<Deal>> getNews(int page);

    Observable<ResponseBody> createPost(NewPostReq body, Uri imageUri);
    Observable<String> sendReport(long id);
}
