package com.jedi.oneplacement.retrofit;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jedi.oneplacement.payloads.ApiResponse;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.JwtAuthResponse;
import com.jedi.oneplacement.payloads.NotifMessage;
import com.jedi.oneplacement.payloads.RoleDto;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.payloads.UserLoginInfo;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.data.UserInstance;

import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiImpl {
    private static final String TAG = "AuthApiImpl";
    private static Api mApi = null;

    private static Api getRetrofitAccessObject() {
        if (mApi == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mApi = retrofit.create(Api.class);
        }
        return mApi;
    }

    public interface ApiCallListener<T> {
        void onResponse(T response);
        void onFailure(int code);
    }

    /* -------- USER AUTH -------- */
    public static void checkUser(String token, ApiCallListener<Map<String, Object>> listener) {
        getRetrofitAccessObject().checkUser("Bearer " + token).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<Map<String, Object>> call, @NonNull Response<Map<String, Object>> response) {
                if (!response.isSuccessful() || response.body().isEmpty()) {
                    listener.onFailure(response.code());
                } else {
                    listener.onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                listener.onFailure(-1);
            }
        });
    }

    public static void loginUser(String userName, String password, ApiCallListener<JwtAuthResponse> listener) {
        getRetrofitAccessObject().loginUser(new UserLoginInfo(userName, password)).enqueue(new Callback<JwtAuthResponse>() {
            @Override
            public void onResponse(@NonNull Call<JwtAuthResponse> call, @NonNull Response<JwtAuthResponse> response) {
                JwtAuthResponse jwt = response.body();
                if (!response.isSuccessful() || jwt == null) {
                    listener.onFailure(response.code());
                    return;
                }
                listener.onResponse(jwt);
            }

            @Override
            public void onFailure(Call<JwtAuthResponse> call, Throwable t) {
                listener.onFailure(-1);
            }
        });
    }

    public static void registerUser(String name, String regNo, String email, String password, String role, ApiCallListener<UserDto> listener){
        User user = new User();
        user.setName(name);
        user.setRegNo(regNo);
        user.setEmail(email);
        user.setPassword(password);
        user.setTpoCredits("10");
        user.setRoleStatus(AppConstants.NOT_OFFERED);
        user.setProfileStatus(AppConstants.NOT_VERIFIED);
        Log.d(TAG, "registerUser: " + user.toString());

        getRetrofitAccessObject().registerUser(role, user)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                        Log.d(TAG, "onResponse: hehehe " + response.body());
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    /* ---------- COMPANIES ---------- */
    public static void addCompany(String token,String cName, String mProfile, String mStipend, String mCtc, String mPPO, String mRole,ApiCallListener<Company> listener){
        Company company = new Company();

        company.setCname(cName);
        company.setProfile(mProfile);
        company.setStipend(mStipend);
        company.setCtc(mCtc);
        company.setPpo(mPPO);
        getRetrofitAccessObject().addCompany(mRole, company, "Bearer " + token)
                .enqueue(new Callback<Company>() {
                    @Override
                    public void onResponse(@NonNull Call<Company> call, @NonNull Response<Company> response) {
                        if(!response.isSuccessful() || response.body() == null)
                        {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<Company> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void getAllCompanies(String token, ApiCallListener<List<Company>> listener){
        Set<RoleDto> roles = UserInstance.getRoles();
        String mRole="";
        for(RoleDto role: roles)
            mRole = role.getRole_name();
        mRole = mRole.substring(5);
        getRetrofitAccessObject().fetchCompanies(mRole,"Bearer " + token)
                .enqueue(new Callback<List<Company>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Company>> call, @NonNull Response<List<Company>> response) {
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        Company company = response.body().get(0);
                        Log.d(TAG, "onResponse: " + company.toString());
                        listener.onResponse(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<Company>> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void registerInC(Company company, String token, ApiCallListener<ApiResponse> listener){
        getRetrofitAccessObject().registerInC(company, "Bearer " + token, UserInstance.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    /* --------- DEVICE TOKEN SETUP ------- */
    public static void setDeviceToken(String devToken, String jwt, ApiCallListener<ApiResponse> listener){
        getRetrofitAccessObject().setupDevToken("Bearer " + jwt, devToken, UserInstance.getId())
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if(!response.isSuccessful() || response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure:err " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }

    /* --------- COMPANY NOTIFICATION -------- */
    public static void sendNotification(String mRole, String token, NotifMessage notifMessage, ApiCallListener<ApiResponse> listener){
        getRetrofitAccessObject().sendNotification("Bearer " + token, mRole, notifMessage)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }
                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    /* --------- EDIT USER DATA --------- */
    public static void uploadImg(String token, MultipartBody.Part img , ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().uploadImage("Bearer " + token, UserInstance.getId(), img)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void uploadResume(String token, MultipartBody.Part resume , ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().uploadResume("Bearer " + token, UserInstance.getId(), resume)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }

    public static void updateUser(String token, String branch, String email, String mno, ApiCallListener<UserDto> listener){
        UserDto userDto = new ModelMapper().map(UserInstance.getUserInstance(), UserDto.class);
        userDto.setBranch(branch);
        userDto.setEmail(email);
        userDto.setPhoneNumber(mno);
        getRetrofitAccessObject().updateUserDetails("Bearer " + token, UserInstance.getId(), userDto)
                .enqueue(new Callback<UserDto>() {
                    @Override
                    public void onResponse(@NonNull Call<UserDto> call, @NonNull Response<UserDto> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        Log.d(TAG, "onResponse: " + response.body());
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<UserDto> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    /* ------- GET ALL USERS -------- */
    public static void getAllUsers(String token,ApiCallListener<List<UserDto>> listener){
        getRetrofitAccessObject().getAllUsers("Bearer " + token)
                .enqueue(new Callback<List<UserDto>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserDto>> call, @NonNull Response<List<UserDto>> response) {
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<UserDto>> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void setCredits(int credits, Integer uid, String token, ApiCallListener<ApiResponse> listener){
        getRetrofitAccessObject().setCredits("Bearer " + token, uid, credits)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }


    public static void verifyProfile(Integer uid, String token, ApiCallListener<ApiResponse> listener){
        getRetrofitAccessObject().verifyProfile("Bearer " + token, uid)
                .enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    public static void searchUsers(String token, String query, ApiCallListener<List<UserDto>> listener){
        getRetrofitAccessObject().searchUsers("Bearer " + token, query)
                .enqueue(new Callback<List<UserDto>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<UserDto>> call, @NonNull Response<List<UserDto>> response) {
                        if(!response.isSuccessful() || response.body()==null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }
                    @Override
                    public void onFailure(Call<List<UserDto>> call, Throwable t) {
                        listener.onFailure(-1);
                    }
                });
    }

    /* ------- DOWNLOAD FILES ------ */
    public static void getResume(Integer uid, String token, ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().getResume(uid, "Bearer " + token)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(!response.isSuccessful() || response.body() == null){
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }

    public static void getImage(Integer uid,String token,ApiCallListener<FileResponse> listener){
        getRetrofitAccessObject().getImage(uid,"Bearer " + token)
                .enqueue(new Callback<FileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FileResponse> call, @NonNull Response<FileResponse> response) {
                        if(response.body()==null) {
                            listener.onFailure(response.code());
                            return;
                        }
                        listener.onResponse(response.body());
                    }

                    @Override
                    public void onFailure(Call<FileResponse> call, Throwable t) {

                        Log.d(TAG, "onFailure: " + t.getMessage());
                        listener.onFailure(-1);
                    }
                });
    }
}