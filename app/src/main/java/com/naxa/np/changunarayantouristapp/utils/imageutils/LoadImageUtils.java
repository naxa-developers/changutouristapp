package com.naxa.np.changunarayantouristapp.utils.imageutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.naxa.np.changunarayantouristapp.R;
import com.naxa.np.changunarayantouristapp.common.ChangunarayanTouristApp;
import com.naxa.np.changunarayantouristapp.utils.CreateAppMainFolderUtils;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class LoadImageUtils {
    private static final String TAG = "LoadImageUtils";

    public static int getImageResIDFromDrawable(@NonNull Context context, String imageName){
        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }

    public static Icon getImageIconFromDrawable(@NonNull Context context, String imageName){

        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), getImageResIDFromDrawable(context, imageName));
        Icon icon = null ;

        if(bitmap!= null) {
            icon = IconFactory.getInstance(context).fromBitmap(convertToMutable(context, bitmap, imageName, 72, 72));

        }
        return  icon;
    }

    public static Bitmap getImageBitmapFromDrawable(@NonNull Context context, String imageName){

        Bitmap bitmap = BitmapFactory.decodeResource(
                context.getResources(), getImageResIDFromDrawable(context, imageName));

        return  bitmap;
    }

    @Nullable
    public static Bitmap getImageBitmapFromStorage(@NonNull String imageFileName){

        try {
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeFile(CreateAppMainFolderUtils.getAppMapDataFolderName()+"/"+imageFileName+".png");
            Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, convertDpToPixel(35), convertDpToPixel(43), false);
            return smallMarker;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static int convertDpToPixel(int dp){
        return dp * ( ChangunarayanTouristApp.getInstance().getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    @Nullable
    public static File getImageFileFromStorage(@NonNull String imageFileName){

        try {

            File file= new File(CreateAppMainFolderUtils.getAppMapDataFolderName(), imageFileName);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts a immutable bitmap to a mutable bitmap. This operation doesn't allocates
     * more memory that there is already allocated.
     *
     * @param imgIn - Source image. It will be released, and should not be used more
     * @return a copy of imgIn, but muttable.
     */
    public static Bitmap convertToMutable(Context context, @NonNull Bitmap imgIn, String imageName, int outHeight, int outWidth) {
        CreateAppMainFolderUtils createAppMainFolderUtils = new CreateAppMainFolderUtils(context, CreateAppMainFolderUtils.appmainFolderName);
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(createAppMainFolderUtils.getAppMediaFolderName()+ File.separator + imageName+".tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes()*height);
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgIn.setHeight(outHeight);
        imgIn.setWidth(outWidth);

        return imgIn;
    }


    public static void imageSaveFileToSpecificDirectory(Bitmap imageToSave, String fileName, String filePath) {

        File file = new File(new File(filePath), fileName+".jpg");
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageToViewFromSrc(@NonNull ImageView imageView, String imageSrc){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        if(!TextUtils.isEmpty(imageSrc)) {
            Glide
                    .with(imageView.getContext())
                    .load(imageSrc)
                    .placeholder(circularProgressDrawable)
                    .centerCrop()
                    .into(imageView);
        }
    }

    public static void loadImageToViewFromSrcWithoutCenterCrop(@NonNull ImageView imageView, String imageSrc){

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(imageView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        if(!TextUtils.isEmpty(imageSrc)) {
            Glide
                    .with(imageView.getContext())
                    .load(imageSrc)
                    .placeholder(circularProgressDrawable)
                    .fitCenter()
                    .into(imageView);
        }
    }

    public static void loadImageToCircleViewFromDrawable(@NonNull ImageView imageView, int resourceID){
//        if(resourceID  null) {
            Glide
                    .with(imageView.getContext())
                    .load(resourceID)
                    .bitmapTransform(new CircleTransform(ChangunarayanTouristApp.getInstance()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(imageView);
//        }
    }

    public static void loadImageToViewFromDrawable(@NonNull ImageView imageView, int resourceID){
//        if(resourceID  null) {
            Glide
                    .with(imageView.getContext())
                    .load(resourceID)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(imageView);
//        }
    }


    public static class ImageGetter implements Html.ImageGetter {

        public Drawable getDrawable(String source) {
            final int[] id = new int[1];
            final Bitmap[] bitmap = {null};

            if (!source.equals("")) {
                Observable.just(source)
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .subscribe(new DisposableObserver<String>() {
                            @Override
                            public void onNext(String s) {
                                try {
                                    File file = new File(Glide.with(ChangunarayanTouristApp.getInstance())
                                            .load(new File(source))
                                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                            .get(), ".jpg");

                                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                                    bmOptions.inJustDecodeBounds = true;
                                    bitmap[0] = BitmapFactory.decodeFile(file.getAbsoluteFile().getAbsolutePath(),bmOptions);
                                    Log.d(TAG, "onNext: "+bitmap[0].getByteCount());
//                    if(bmOptions.outWidth != -1 && bmOptions.outHeight != -1){

//                    }

                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                id[0] = R.mipmap.ic_launcher_foreground;
                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {
                            }
                        });
            }



            else {
                return null;
            }

//            Drawable d = ISET.getInstance().getResources().getDrawable(id);
//            d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
//            return d;

            BitmapDrawable drawable = new BitmapDrawable(ChangunarayanTouristApp.getInstance().getResources(), bitmap[0]);
            drawable.setBounds(0,0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
            return drawable;

        }

        public String getSrc(String source){
            String source1 = null;
            return source1;
        }

    }


    public static void downloadAndSaveImageToStorage(AppCompatActivity context , String imageName, String imageUrl){

        String [] imageUlrAndLocationToSave = {imageName, imageUrl, CreateAppMainFolderUtils.getAppMapDataFolderName()};
        ImageSaveTask imageSaveTask = new ImageSaveTask(context);
        imageSaveTask.execute(imageUlrAndLocationToSave);
    }
}
