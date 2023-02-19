package com.jedi.oneplacement.data;

import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.retrofit.Api;

import java.util.List;

public class Repository {

    // TODO : singleton bnao:

    public interface ResourceListener<T> {

        void onSuccess(T data);

        void onFailure(String errMsg);
    }


    private static List<User> randomList;

    public static <T> void fun(ResourceListener<List<User>> listener, boolean forceFetch) {
        if (!randomList.isEmpty() || !forceFetch) {
            listener.onSuccess(randomList);
            return;
        }

        // Api Calls here:
    }
}
