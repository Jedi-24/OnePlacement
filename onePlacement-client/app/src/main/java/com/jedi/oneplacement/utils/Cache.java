package com.jedi.oneplacement.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.DefaultLinkHandler;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cache {
    private static final String TAG = "Cache";
    private static boolean resumeLoader = false;

    public static Bitmap readImgFromCache(Context context, Integer userId) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO + "_" + userId);
        Bitmap b = null;
        try {
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (IOException e) {
            Log.d(TAG, "readFromCache: " + e.getMessage());
        }
        return b;
    }

    public static void updateResCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.RESUME_BAK);
        File f_ = new File(folder, AppConstants.RESUME + "_" + 0);
        Log.d(TAG, "updateCache: " + f_.delete());
        Log.d(TAG, "updateCache: " + f.renameTo(f_));
    }

    public static void updateImgCache(Context context) {
        File folder = context.getCacheDir();
        File f = new File(folder, AppConstants.USER_PHOTO_BAK);
        File f_ = new File(folder, AppConstants.USER_PHOTO + "_" + 0);
        Log.d(TAG, "updateCache: " + f_.delete());
        Log.d(TAG, "updateCache: " + f.renameTo(f_));
    }

    public static void ClearCache(Context context) {
        File folder = context.getCacheDir();
        for(File f:folder.listFiles())
            f.delete();
    }
    public static void writeToCache(Context context, byte[] byteData, String type, Integer userId) {
        String fileName = "";
        if (type.matches("I"))
            fileName = AppConstants.USER_PHOTO;
        else
            fileName = AppConstants.RESUME;

        File folder = context.getCacheDir();
        File f = new File(folder, fileName + "_" + userId);
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

    public static void configurePdf(PDFView pdf, byte[] byteData) {
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
}