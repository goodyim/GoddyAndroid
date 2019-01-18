package im.goody.android.data.network;

import java.util.List;
import java.util.Map;

import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Feedback;
import im.goody.android.data.dto.Follower;
import im.goody.android.data.dto.HelpInfo;
import im.goody.android.data.dto.Participant;
import im.goody.android.data.dto.User;
import im.goody.android.data.network.req.AchievementsReq;
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
    @GET("zones")
    Observable<String> getHelpInfo();

    @POST("zones")
    Observable<ResponseBody> sendHelpInfo(@Body HelpInfo body);

    @Multipart
    @POST("users")
    Observable<UserRes> registerUser(@Header("fcmToken") String fcmToken,
                                     @PartMap Map<String, RequestBody> params);

    @GET("users/get_token")
    Observable<UserRes> loginUser(@Header("fcmToken") String fcmToken,
                                  @Query("user_name") String name,
                                  @Query("password") String password);

    @GET("users/register_fcm_token")
    Observable<ResponseBody> sendFcmToken(@Header("fcmToken") String fcmToken);

    @GET("good_deals")
    Observable<List<Deal>> getDeals(@Query("user_id") String userId,
                                    @Query("page") int page,
                                    @Query("type") String contentType);

    @Multipart
    @POST("good_deals")
    Observable<ResponseBody> uploadDeal(@PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part file);

    @Multipart
    @PUT("good_deals/{id}")
    Observable<ResponseBody> updateDeal(@Path("id") long id,
                                        @PartMap Map<String, RequestBody> params,
                                        @Part MultipartBody.Part file);

    @DELETE("good_deals/{id}")
    Observable<ResponseBody> deletePost(@Path("id") long id);

    @GET("good_deals/{id}")
    Observable<Deal> getDeal(@Path("id") long id);

    @POST("comments")
    Observable<CommentRes> sendComment(@Query("good_deal_id") long id,
                                       @Body NewCommentReq comment);

    @DELETE("comments/{id}")
    Observable<ResponseBody> deleteComment(@Path("id") long id);

    @POST("good_deals/{id}/like")
    Observable<Deal> like(@Path("id") long id);

    @POST("good_deals/{id}/participate")
    Observable<ParticipateRes> participate(@Path("id") long id);

    @POST("good_deals/{id}/close")
    Observable<EventStateRes> closeEvent(@Path("id") long id,
                                         @Body AchievementsReq body);

    @GET("users/{id}")
    Observable<User> getUserProfile(@Path("id") String id);

    @POST("users/{id}/follow")
    Observable<FollowRes> changeFollowState(@Path("id") String id);


    @GET("events")
    Observable<List<Deal>> getEvents(@Query("user_id") String userId,
                                     @Query("state") String state);


    @GET("users/reset_password")
    Observable<ResponseBody> recoverPassword(@Query("email") String email);

    @DELETE("users/destroy_session")
    Observable<ResponseBody> logout();

    @GET("notifications")
    Observable<List<Feedback>> loadNotifications();

    @GET("events/{id}/members")
    Observable<List<Participant>> getParticipants(@Path("id") long id);

    @GET("users/{id}/followers")
    Observable<List<Follower>> getFollowers(@Path("id") long id);

    @Multipart
    @PUT("users/{id}")
    Observable<User> updateAvatar(@Path("id") String id,
                                  @Part MultipartBody.Part avatar);
}
