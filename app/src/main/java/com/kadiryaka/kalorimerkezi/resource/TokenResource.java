package com.kadiryaka.kalorimerkezi.resource;

import com.kadiryaka.kalorimerkezi.dto.LoginRequest;
import com.kadiryaka.kalorimerkezi.service.TokenService;
import com.kadiryaka.kalorimerkezi.util.KaloriMerkeziRestAdapter;

import retrofit.Callback;

@SuppressWarnings("unchecked")
public class TokenResource {
    private TokenService tokenService;

    public TokenResource() {
        tokenService = new KaloriMerkeziRestAdapter().getRestAdapter().create(TokenService.class);
    }

    public void login(LoginRequest loginRequest, Callback callback) {
        tokenService.login("1", loginRequest, callback);
    }

    public void logout(String token, Callback callback) {
        tokenService.logout(token, callback);
    }
}
