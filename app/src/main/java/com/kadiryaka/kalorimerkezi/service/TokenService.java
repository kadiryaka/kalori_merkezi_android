package com.kadiryaka.kalorimerkezi.service;

import com.kadiryaka.kalorimerkezi.dto.LoginRequest;
import com.kadiryaka.kalorimerkezi.dto.LoginResponse;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;

public interface TokenService {
    @POST("/token/login")
    void login(@Header("platform") String platform, @Body LoginRequest loginRequest, Callback<LoginResponse> callback);

    @GET("/token/logout")
    void logout(@Header("kalori_token") String token, Callback<Response> callback);
}