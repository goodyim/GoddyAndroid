package im.goody.android.data;

import android.net.Uri;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.User;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.NewCommentReq;
import im.goody.android.data.network.req.NewEventReq;
import im.goody.android.data.network.req.NewPostReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.CommentRes;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import okhttp3.ResponseBody;

public interface IRepository {

    Observable<UserRes> login(LoginReq data);
    Observable<UserRes> register(RegisterReq data, Uri avatarUri);
    boolean isSigned();

    boolean isFirstLaunch();
    void firstLaunched();
    <T> T getError(Throwable t, Class<T> tClass);

    Observable<List<Deal>> getNews(int page);
    Observable<Deal> getDeal(long id);
    Observable<CommentRes> sendComment(long dealId, NewCommentReq body);

    Observable<ResponseBody> createPost(NewPostReq body, Uri imageUri);
    Observable<ResponseBody> createEvent(NewEventReq body, Uri imageUri);
    Observable<String> sendReport(long id);

    Observable<User> getUserProfile(long id);

    Observable<Deal> likeDeal(long id);
    Observable<ParticipateRes> changeParticipateState(long id);
    Observable<EventStateRes> changeEventState(long id);

    UserRes getUserData();

    void logout();
}
