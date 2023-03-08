package com.jedi.oneplacement.data;

import static com.jedi.oneplacement.utils.Cache.configurePdf;
import static com.jedi.oneplacement.utils.Cache.writeToCache;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.jedi.oneplacement.payloads.Company;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.payloads.User;
import com.jedi.oneplacement.payloads.UserDto;
import com.jedi.oneplacement.retrofit.ApiImpl;
import com.jedi.oneplacement.utils.AppConstants;
import com.jedi.oneplacement.utils.Cache;
import com.jedi.oneplacement.utils.UserInstance;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
// this class deals with all the data, whatever fragment / activity needs some data, it is provided by this class only:
public class Repository {
    private static final String TAG = "Repository";
    // TODO : singleton class: to use a single instance everywhere
    private static Repository instance = null;
    private static boolean resumeLoader = false;

    private Repository() {
    }

    public static Repository getRepoInstance() {
        if (instance == null) {
            instance = new Repository();
        }
        return instance;
    }

    public interface ResourceListener<T> {
        void onSuccess(T data);

        void onFailure(String errMsg);
    }

    private static List<User> randomList;
    private static List<UserDto> usersList = new ArrayList<>();
    private static List<Company> companyList = new ArrayList<>();

    // setters for lists: for search feature:
    public void setUsersList(List<UserDto> filteredUsers) {
        usersList = filteredUsers;
    }

    public void setCompanyList(List<Company> filteredCompanies) {
        companyList = filteredCompanies;
    }

    public void fetchCompanies(Context ctx, ResourceListener<List<Company>> companiesListener, boolean forceFetch) {
        Log.d(TAG, "fetchCompanies: " + companyList.isEmpty() + " " + forceFetch + " jaya k ");
        if (!companyList.isEmpty() && !forceFetch) {
            companiesListener.onSuccess(companyList);
            return;
        }

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllCompanies(jwt, new ApiImpl.ApiCallListener<List<Company>>() {
            @Override
            public void onResponse(List<Company> response) {
                Log.d(TAG, "onResponse: nbaaaar");
                companyList = response;
                companiesListener.onSuccess(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, "Error in fetching Companies !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void fetchUsers(Context ctx, ResourceListener<List<UserDto>> listener, boolean forceFetch) {
        if (!usersList.isEmpty() && !forceFetch) {
            listener.onSuccess(usersList);
            return;
        }

        SharedPreferences sharedPreferences = ctx.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String jwt = sharedPreferences.getString(AppConstants.JWT, null);
        ApiImpl.getAllUsers(jwt, new ApiImpl.ApiCallListener<List<UserDto>>() {
            @Override
            public void onResponse(List<UserDto> response) {
                usersList = response;
                listener.onSuccess(response);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(ctx, code + " Failed to fetch users !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void getImage(Context context, Integer userId, ImageView userImg, ImageView tbImg) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);

        Bitmap b = Cache.readImgFromCache(context, userId);
        if (b != null) {
            userImg.setImageBitmap(b);
            if (tbImg != null) tbImg.setImageBitmap(b);
            return;
        }
        ApiImpl.getImage(userId == 0 ? UserInstance.getId() : userId, token, new ApiImpl.ApiCallListener<FileResponse>() {
            @Override
            public void onResponse(FileResponse response) {
                byte[] byteData = Base64.decode(response.getFileName(), 0);
                Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                userImg.setImageBitmap(bm);
                if (tbImg != null) tbImg.setImageBitmap(bm);
                writeToCache(context, byteData, "I", userId);
            }

            @Override
            public void onFailure(int code) {
                if (code == -1)
                    Toast.makeText(context, "Failed to load the image...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static boolean getResume(Context context, PDFView pdf, Integer userId) {
        Log.d(TAG, "getResume:usserr iddd " + userId);
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);

        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.RESUME + "_" + userId);
        if (f.exists()) {
            try {
                byte[] byteArray = Files.readAllBytes(f.toPath());
                configurePdf(pdf, byteArray);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ApiImpl.getResume(userId == 0 ? UserInstance.getId() : userId, token, new ApiImpl.ApiCallListener<FileResponse>() {
            @Override
            public void onResponse(FileResponse response) {
                byte[] byteData = Base64.decode(response.getFileName(), 0);
                configurePdf(pdf, byteData);
                writeToCache(context, byteData, "R", userId);
                resumeLoader = true;
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(context, code + " Failed to Open Resume.", Toast.LENGTH_SHORT).show();
            }
        });
        return resumeLoader;
    }

    // sample function definition:
    public void fun(ResourceListener<List<User>> listener, boolean forceFetch) {
        if (!randomList.isEmpty() || !forceFetch) {
            listener.onSuccess(randomList);
            return;
        }
        // Api Calls here:
    }
}
