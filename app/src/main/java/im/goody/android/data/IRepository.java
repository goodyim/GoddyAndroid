package im.goody.android.data;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.RegisterReq;
import io.reactivex.Observable;

public interface IRepository {

    Observable<String> login(LoginReq data);
    Observable<String> register(RegisterReq data);
    boolean isSigned();

    boolean isFirstLaunch();
    void firstLaunched();

    Observable<List<Deal>> getNews();
}
