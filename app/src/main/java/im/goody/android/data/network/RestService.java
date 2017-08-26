package im.goody.android.data.network;

import im.goody.android.data.network.req.LoginReq;
import im.goody.android.data.network.req.RegisterReq;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RestService {

    @POST("users")
    Observable<UserRes> registerUser(@Body RegisterReq registerReq);

    @GET("users/show")
    Observable<UserRes> loginUser(@Header("X-User-Token") String userToken,
                                  @Body LoginReq loginReq);
}
