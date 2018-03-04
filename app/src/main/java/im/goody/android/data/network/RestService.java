package im.goody.android.data.network;

import java.util.List;
import java.util.Map;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Feedback;
import im.goody.android.data.dto.User;
import im.goody.android.data.network.req.NewCommentReq;
import im.goody.android.data.network.res.CommentRes;
import im.goody.android.data.network.res.EventStateRes;
import im.goody.android.data.network.res.FollowRes;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.data.network.res.UserRes;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestService {

    @Multipart
    @POST("users")
    Observable<UserRes> registerUser(
            @Header("fcmToken") String fcmToken,
            @PartMap Map<String, RequestBody> params);

    @GET("users/get_token")
    Observable<UserRes> loginUser(@Header("fcmToken") String fcmToken,
                                  @Query("user_name") String name,
                                  @Query("password") String password);

    @GET("users/register_fcm_token")
    Observable<ResponseBody> sendFcmToken(@Header("fcmToken") String fcmToken,
                                          @Header("X-User-Token") String UserToken);

    @GET("good_deals")
    Observable<List<Deal>> getDeals(@Header("X-User-Token") String token,
                                    @Query("user_id") String userId,
                                    @Query("page") int page);

    @Multipart
    @POST("good_deals")
    Observable<ResponseBody> uploadDeal(@Header("X-User-Token") String token,
                                        @PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part file);

    @Multipart
    @PUT("good_deals/{id}")
    Observable<ResponseBody> updateDeal(@Header("X-User-Token") String token,
                                        @Path("id") long id,
                                        @PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part file);

    @DELETE("good_deals/{id}")
    Observable<ResponseBody> deletePost(@Header("X-User-Token") String token,
                                        @Path("id") long id);

    @GET("good_deals/{id}")
    Observable<Deal> getDeal(@Header("X-User-Token") String token,
                             @Path("id") long id);

    @POST("comments")
    Observable<CommentRes> sendComment(@Header("X-User-Token") String token,
                                       @Query("good_deal_id") long id,
                                       @Body NewCommentReq comment);

    @POST("good_deals/{id}/like")
    Observable<Deal> like(@Header("X-User-Token") String token,
                          @Path("id") long id);

    @POST("good_deals/{id}/participate")
    Observable<ParticipateRes> participate(@Header("X-User-Token") String token,
                                           @Path("id") long id);

    @POST("good_deals/{id}/close")
    Observable<EventStateRes> changeEventState(@Header("X-User-Token") String token,
                                               @Path("id") long id);

    @GET("users/{id}")
    Observable<User> getUserProfile(@Header("X-User-Token") String token,
                                    @Path("id") String id);

    @POST("users/{id}/follow")
    Observable<FollowRes> changeFollowState(@Header("X-User-Token") String token,
                                            @Path("id") String id);


    @GET("events")
    Observable<List<Deal>> getActiveEvents(@Header("X-User-Token") String token);


    @GET("users/reset_password")
    Observable<ResponseBody> recoverPassword(@Query("email") String email);

    @DELETE("users/destroy_session")
    Observable<ResponseBody> logout(@Header("X-User-Token") String token);

    @GET("notifications")
    Observable<List<Feedback>> loadNotifications(@Header("X-User-Token") String token);
}
