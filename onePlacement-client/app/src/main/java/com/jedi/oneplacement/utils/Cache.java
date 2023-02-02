package com.jedi.oneplacement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.retrofit.ApiImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

public class Cache {
    private static final String TAG = "Cache";
    public static Bitmap readFromCache(Context context){
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO);
        Bitmap b = null;
        try{
            new FileInputStream(f).read();
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void updateCache(Context context){
        // not a good way to do it.
        File folder = context.getCacheDir();
        File f = new File(folder,AppConstants.USER_PHOTO_BAK);
        File f_ = new File(folder,AppConstants.USER_PHOTO);
        Log.d(TAG, "updateCache: " + f_.delete());
        Log.d(TAG, "updateCache: " + f.renameTo(f_));
    }

    public static void removeImgFromCache(Context context){
        File folder = context.getCacheDir();
        File f = new File(folder,AppConstants.USER_PHOTO);
        f.delete();
    }

    public static void writeToCache(Context context, byte[] byteData){
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO);
        Log.d(TAG, "writeToCache: " + f.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(byteData);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try{
                assert fos != null;
                fos.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void getImage(Context context, ImageView userImg, ImageView tbImg) {
        Log.d(TAG, "getImage: haha " + UserInstance.getId());

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);
        Bitmap b = Cache.readFromCache(context);

        if(b !=null){
            userImg.setImageBitmap(b);
            tbImg.setImageBitmap(b);
            Toast.makeText(context, "Image is loaded from Internal Cache!", Toast.LENGTH_SHORT).show();
            return;
        }


        ApiImpl.getImage(UserInstance.getId(), token, new ApiImpl.ApiCallListener<FileResponse>() {
            @Override
            public void onResponse(FileResponse response) {
                byte[] byteData = Base64.decode(response.getFileName(),0);
                Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                userImg.setImageBitmap(bm);
                tbImg.setImageBitmap(bm);

                Cache.writeToCache(context, byteData);
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(context, "Failed to load the image...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}