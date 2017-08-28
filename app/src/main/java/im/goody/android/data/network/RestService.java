package im.goody.android.data.network;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestService {

    @POST("users")
    Observable<UserRes> registerUser(@Body RegisterReq registerReq);

    @GET("users/show") // TODO change path to actual after server will be ready
    Observable<UserRes> loginUser(@Body LoginReq loginReq);

    @GET("good_deals")
    Observable<List<Deal>> getDeals(@Header("X-User-Token") String token, @Query("page") int page);
}
