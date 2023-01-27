package com.jedi.oneplacement.payloads;

public class JwtAuthResponse {
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "JwtAuthResponse{" +
                "token='" + token + '\'' +
                '}';
    }
}
