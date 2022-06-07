package com.i2donate.RetrofitAPI;


import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

   /* @GET("login")
    Call<LoginResponse> getTopRatedMovies(@Query("api_key") String apiKey);*/
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"})
   //// @FormUrlEncoded    // annotation that used with POST type request
    @POST("login")
    Call<JsonObject> userLogin(@Body JsonObject loginInput);

    @POST("country_list")
    Call<JsonObject> countryAPI(@Body JsonObject loginInput);


    @POST("statecity")
    Call<JsonObject> Statecity(@Body JsonObject loginInput);


    @POST("register")
    Call<JsonObject> newuserregister(@Body JsonObject registerInput);


    @Multipart
    @POST("register")
    Call<CommonResponseModel> Business_Register_optional(@Part("business_name") RequestBody B_name, @Part("name") RequestBody name, @Part("phone") RequestBody phone, @Part("email") RequestBody email, @Part("device_id") RequestBody d_id,
                                                @Part("country") RequestBody country, @Part("type") RequestBody types,
                                                @Part("terms") RequestBody terms,
                                                         @Part("addr1") RequestBody addr1,
                                                         @Part("addr2") RequestBody addr2,
                                                         @Part("state") RequestBody state,
                                                         @Part("city") RequestBody city,
                                                         @Part("zip") RequestBody zip,
                                                         @Part MultipartBody.Part incorp,
                                                   @Part MultipartBody.Part tax);
    @Multipart
    @POST("register")
    Call<CommonResponseModel> Business_Register(@Part("business_name") RequestBody B_name, @Part("name") RequestBody name, @Part("phone") RequestBody phone, @Part("email") RequestBody email, @Part("device_id") RequestBody d_id,
                                                      @Part("country") RequestBody country, @Part("type") RequestBody types,
                                                      @Part("terms") RequestBody terms,
                                                    @Part("addr1") RequestBody addr1,
                                                    @Part("addr2") RequestBody addr2,
                                                    @Part("state") RequestBody state,
                                                    @Part("city") RequestBody city,
                                                    @Part("zip") RequestBody zip,
                                                      @Part MultipartBody.Part incorp,
                                                      @Part MultipartBody.Part tax,
                                                      @Part MultipartBody.Part dba);

    Call<JsonObject> register(@Body JsonObject registerInput);

    @POST("social_login")
    Call<JsonObject> socialmedialogin(@Body JsonObject socialmediaInput);

    @POST("update_profile")
    Call<JsonObject> update_profile(@Body JsonObject update_profile);

    @POST("categories")
    Call<JsonObject> Adavncecategories(@Body JsonObject categories);

    @POST("charity_list")
    Call<JsonObject> Charitylist(@Body JsonObject categories);

    @POST("charity_like_dislike")
    Call<JsonObject> charity_like_dislike(@Body JsonObject categories);

    @POST("charity_following")
    Call<JsonObject> charity_following(@Body JsonObject categories);

    @POST("likes_and_followings")
    Call<JsonObject> likes_and_followings(@Body JsonObject categories);

    @POST("update_password")
    Call<JsonObject> change_password(@Body JsonObject categories);

    @POST("forgot_password")
    Call<JsonObject> forgot_password(@Body JsonObject categories);

    @POST("transaction_list")
    Call<JsonObject> transaction_list(@Body JsonObject categories);

    @POST("verify_otp")
    Call<JsonObject> verify_otp(@Body JsonObject categories);

    @POST("update_password")
    Call<JsonObject> update_password(@Body JsonObject categories);

    @POST("searchname")
    Call<JsonObject> search_name(@Body JsonObject categories);
    /* @FormUrlEncoded
    @POST("forgototpemailid") // specify the sub url for our base
    Call<ForgototpResponse> Email(
            @Field("EmailId") String EmailId);*/
    @POST("transaction")
    Call<JsonObject> updatePayment(@Body JsonObject categories);

    @POST("device_update")
    Call<JsonObject> device_update(@Body JsonObject device_update);

    @POST("notification")
    Call<JsonObject> notification(@Body JsonObject user_id);

    @POST("braintree_client_token")
    Call<String> getbraintree();

    @POST("transaction")
    Call<JsonObject> sentamountbraintreeAPI(@Body JsonObject jsonObject);
}