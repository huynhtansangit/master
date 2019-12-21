package com.example.uitest;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserInterface {

    /*------------ GET REGION ----------*/

    @GET("api/profile")
    Call<ResponseBody> getInfoUser(@Header("Authorization") String authToken);

    @GET("api/profile/{user_id}")
    Call<Current_user_cache> getUserbyId(@Header("Authorization") String authToken,@Path(value = "user_id",encoded = true)String iduser);

    @GET("api/post")
    Call<List<ItemObj>> getProducts(@Header("Authorization") String authToken);

    @GET("api/post/tag/{tag}")
    Call<List<ItemObj>> getProductsByTag(@Header("Authorization") String authToken,@Path(value = "tag",encoded = true)String tag);


    @GET ("api/post/{id}")
    Call<ItemObj> getDetailProduct(@Header("Authorization") String authToken,@Path(value = "id",encoded = true)String id);

    @GET("api/ypost")
    Call<ResponseBody> getProductsByUser(@Header("Authorization") String authToken);
    /*------------ POST REGION ----------*/

    @Multipart
    @POST("api/profile")
    Call<ResponseBody>  UploadImage(
        @Header("Authorization") String authToken,
        @Part MultipartBody.Part photo);

    @POST("api/profile")
    Call<ResponseBody>  EditProfile(@Header("Authorization") String authToken, @Body HashMap<String,Object> body);

    @POST ("api/logout")
    Call<ResponseBody> Logout(@Header("Authorization") String authToken);

    @POST ("api/changepassword")
    Call<ResponseBody> changePassword(@Header("Authorization") String authToken,@Body HashMap<String,Object> body);

    @Multipart
    @POST ("api/post")
    Call<ResponseBody> PostNewItem (@Header("Authorization")String authToken, @PartMap() HashMap<String,RequestBody> partmap, @Part MultipartBody.Part photo);

    @POST  ("api/register")
    Call<ResponseBody> Register (@Body HashMap<String,String> register_map);

    @POST  ("api/create")
    Call<ResponseBody> sendMailToReset (@Body HashMap<String,String> email);

    @POST  ("api/reset")
    Call<ResponseBody> submitResetRequest (@Body HashMap<String,String> params);

    @POST ("api/post/{id}")
    Call<ResponseBody> postCmt(@Header("Authorization") String authToken,@Path(value = "id",encoded = true)String id, @Body HashMap<String,String> cmt);
    /*------------ DELETE REGION ----------*/
    @DELETE ("api/post/{id}")
    Call<ResponseBody> deleteitem (@Header("Authorization") String authToken,@Path(value = "id",encoded = true)String id);

}
