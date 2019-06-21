package com.naxa.np.changunarayantouristapp.utils.imageutils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageSaveTask  extends AsyncTask< String, Void, Void> {
    private AppCompatActivity context;
    private static final String TAG = "ImageSaveTask";

    public ImageSaveTask(AppCompatActivity context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (params == null || params.length < 2) {
            throw new IllegalArgumentException("You should offer 2 params, the first for the image source url, and the other for the destination file save path");
        }

        String imgName = params[0];
        String src = params[1];
        String folderPath = params[2];

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context)
                        .load(src)
                        .asBitmap()
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL) {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                                File targetFile = new File(folderPath+ File.separator +  imgName);
                                if (targetFile.exists()) {
                                    if(targetFile.delete()){
                                        saveImage(resource,imgName, folderPath);
                                    }
                                }else {
                                    saveImage(resource,imgName, folderPath);
                                }
                            }
                        });
            }
        });

        return null;
    }

    private String saveImage(Bitmap image, String imageName, String storagePath) {
        String savedImagePath = null;

        String imageFileName = imageName + ".jpg";
        File storageDir = new File(
                storagePath);

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);

            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
                        Log.d("ImageSaveTask", "doInBackground: "+imageFileName+ "saved successfully ");
                        Log.d("ImageSaveTask", "image saved path"+ savedImagePath);

        }
        return savedImagePath;
    }
}