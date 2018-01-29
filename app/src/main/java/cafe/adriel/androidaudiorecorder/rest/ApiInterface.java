package cafe.adriel.androidaudiorecorder.rest;


import cafe.adriel.androidaudiorecorder.model.AudioResponse;
import cafe.adriel.androidaudiorecorder.model.Respose;
import cafe.adriel.androidaudiorecorder.model.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {
    @FormUrlEncoded
    @POST("api/auth")
    Call<User> getTokenAuthen(@Field("email") String email, @Field("password") String password);
    @FormUrlEncoded
    @POST("api/getContent")
    Call<AudioResponse> getContent(@Field("status") String status, @Header("Authorization") String token);
    @FormUrlEncoded
    @POST("api/getStatistic")
    Call<AudioResponse> getStatistic(@Field("startDate") String status,@Field("endDate") String endDate,@Field("type") int type, @Header("Authorization") String token);
    @Multipart
    @POST("api/saveAudio")
    Call<Respose> saveAudio(@Part MultipartBody.Part file , @Query("id") String id, @Header("Authorization") String token);
}
