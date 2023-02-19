package com.jedi.oneplacement.payloads;

public class ApiResponse {
    private String message;
    private boolean success;

    public ApiResponse() {
        // todo : calls to be made from fragments and activities asking for data :
//        Repository.fun(new Repository.ResourceListener<LightState>() {
//            @Override
//            public void onSuccess(LightState data) {
//                kjdsbkjds
//            }
//
//            @Override
//            public void onFailure(String errMsg) {
//            }
//        });
    }

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
