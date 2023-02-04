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

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.jedi.oneplacement.payloads.FileResponse;
import com.jedi.oneplacement.retrofit.ApiImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class Cache {
    private static final String TAG = "Cache";
    private static boolean resumeLoader = false;

    public static Bitmap readFromCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO);
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static void updateResCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.RESUME_BAK);
        File f_ = new File(folder, AppConstants.RESUME);
        Log.d(TAG, "updateCache: " + f_.delete());
        Log.d(TAG, "updateCache: " + f.renameTo(f_));
    }

    public static void updateImgCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO_BAK);
        File f_ = new File(folder, AppConstants.USER_PHOTO);
        Log.d(TAG, "updateCache: " + f_.delete());
        Log.d(TAG, "updateCache: " + f.renameTo(f_));
    }

    public static void removeResumeFromCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.RESUME);
        f.delete();
    }

    public static void removeImgFromCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO);
        f.delete();
    }

    public static void writeToCache(Context context, byte[] byteData, String type) {
        String fileName = "";
        if (type.matches("I"))
            fileName = AppConstants.USER_PHOTO;
        else
            fileName = AppConstants.RESUME;

        File folder = context.getCacheDir();
        File f = new File(folder, fileName);
        Log.d(TAG, "writeToCache: " + f.getAbsolutePath());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(byteData);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean getResume(Context context, PDFView pdf) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);

        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.RESUME);
        if(f.exists()){
            try{
                byte[] byteArray = Files.readAllBytes(f.toPath());
                configurePdf(pdf, byteArray);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ApiImpl.getResume(UserInstance.getId(), token, new ApiImpl.ApiCallListener<FileResponse>() {
            @Override
            public void onResponse(FileResponse response) {
                System.out.println(response.getFileName());
                byte[] byteData = Base64.decode(response.getFileName(), 0);
                configurePdf(pdf, byteData);
                writeToCache(context,byteData,"R");
                resumeLoader = true;
            }

            @Override
            public void onFailure(int code) {
                Toast.makeText(context, code + " Failed to Open Resume.", Toast.LENGTH_SHORT).show();
            }
        });
        return resumeLoader;
    }

    private static void configurePdf(PDFView pdf, byte[] byteData){
        pdf.fromBytes(byteData).enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .autoSpacing(true) // add dynamic spacing to fit each page on its own on the screen
                .linkHandler(new DefaultLinkHandler(pdf))
                .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                .pageSnap(true) // snap pages to screen boundaries
                .pageFling(false) // make a fling change only a single page like ViewPager
                .nightMode(false) // toggle night mode
                .load();
    }

    public static void getImage(Context context, ImageView userImg, ImageView tbImg) {
        Log.d(TAG, "getImage: haha " + UserInstance.getId());

        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.APP_NAME, Context.MODE_PRIVATE);
        String token = sharedPreferences.getString(AppConstants.JWT, null);
        Bitmap b = Cache.readFromCache(context);

        if (b != null) {
            userImg.setImageBitmap(b);
            tbImg.setImageBitmap(b);
            return;
        }

        ApiImpl.getImage(UserInstance.getId(), token, new ApiImpl.ApiCallListener<FileResponse>() {
            @Override
            public void onResponse(FileResponse response) {
                System.out.println(response.getFileName());
                byte[] byteData = Base64.decode(response.getFileName(), 0);
                Bitmap bm = BitmapFactory.decodeByteArray(byteData, 0, byteData.length);
                userImg.setImageBitmap(bm);
                tbImg.setImageBitmap(bm);

                Cache.writeToCache(context, byteData, "I");
            }

            @Override
            public void onFailure(int code) {
                if(code==-1)
                    Toast.makeText(context, "Failed to load the image...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}