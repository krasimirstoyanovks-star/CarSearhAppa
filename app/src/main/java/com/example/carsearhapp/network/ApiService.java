package com.example.carsearhapp.network;

import com.example.carsearhapp.model.Part;
import com.example.carsearhapp.model.Vehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("getVehicle.php")
    Call<Vehicle> getVehicle(
            @Query("vin") String vin
    );

    @GET("getParts.php")
    Call<List<Part>> getParts(
            @Query("make") String make,
            @Query("model") String model,
            @Query("year") String year
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<Void> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login.php")
    Call<UserResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("forgot_password.php")
    Call<UserResponse> forgotPassword(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("addToGarage.php")
    Call<Void> addToGarage(
            @Field("user_id") int userId,
            @Field("vin") String vin
    );

    @GET("getGarage.php")
    Call<List<Vehicle>> getGarage(
            @Query("user_id") int userId
    );

    @FormUrlEncoded
    @POST("removeFromGarage.php")
    Call<Void> removeFromGarage(
            @Field("user_id") int userId,
            @Field("vin") String vin
    );

    @FormUrlEncoded
    @POST("clearGarage.php")
    Call<Void> clearGarage(
            @Field("user_id") int userId
    );
}