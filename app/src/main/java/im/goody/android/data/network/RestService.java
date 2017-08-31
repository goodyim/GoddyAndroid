package im.goody.android.data.network;

import java.util.List;

import im.goody.android.data.dto.Deal;
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

    @GET("users/get_token")
    Observable<UserRes> loginUser(@Query("user_name") String name,
                                  @Query("password") String password);

    @GET("good_deals")
    Observable<List<Deal>> getDeals(@Header("X-User-Token") String token, @Query("page") int page);
}
