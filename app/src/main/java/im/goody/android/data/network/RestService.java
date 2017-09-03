package im.goody.android.data.network;

import java.util.List;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RestService {

    @Multipart
    @POST("users")
    Observable<UserRes> registerUser(
            @Part("user[email]") String email,
            @Part("user[user_name]") String name,
            @Part("user[password]") String password,
            @Part MultipartBody.Part file
    );

    @GET("users/get_token")
    Observable<UserRes> loginUser(@Query("user_name") String name,
                                  @Query("password") String password);

    @GET("good_deals")
    Observable<List<Deal>> getDeals(@Header("X-User-Token") String token, @Query("page") int page);

    @Multipart
    @POST("good_deals")
    Observable<ResponseBody> uploadDeal(@Header("X-User-Token") String token,
                                        @Part("good_deal[description]") RequestBody description,
                                        @Part("good_deal[category_id]") int category,
                                        @Part("good_deal[private]") boolean isPrivate,
                                        @Part MultipartBody.Part file);
}
