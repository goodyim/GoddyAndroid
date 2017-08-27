package im.goody.android.data;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;

public interface IRepository {

    Observable<UserRes> login(LoginReq data);
    Observable<UserRes> register(RegisterReq data);
    boolean isSigned();

    boolean isFirstLaunch();
    void firstLaunched();

    Observable<List<Deal>> getNews();

    Observable<String> createPost(NewPostReq body);
    Observable<String> sendReport(long id);
}
