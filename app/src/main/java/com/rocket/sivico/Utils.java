package com.rocket.sivico;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rocket.sivico.Data.Report;
import com.rocket.sivico.Interfaces.OnImageUploaded;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Utils {
    private static final String TAG = Utils.class.getSimpleName();
    public static SimpleDateFormat sivicoDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static SimpleDateFormat sivicoDateAndHourFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
    public static SimpleDateFormat sivicoHourFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    public static SimpleDateFormat sivicoAge = new SimpleDateFormat("yyyy", Locale.getDefault());

    public static String getHour(String timestamp) {
        return getHour(Long.parseLong(timestamp));
    }

    public static String getHour(long timestamp) {
        return sivicoHourFormat.format(new Date(timestamp * 1000));
    }

    public static String getFormatDate(String timestamp) {
        return getFormatDate(Long.parseLong(timestamp));
    }

    public static String getFormatDate(long timestamp) {
        return sivicoDateFormat.format(new Date(timestamp * 1000));
    }

    public static String getAge(String timestamp) {
        return getAge(Long.parseLong(timestamp));
    }

    public static String getAge(long timestamp) {
        int yearEnd = Calendar.getInstance().get(Calendar.YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(timestamp * 1000));
        int yearInit = calendar.get(Calendar.YEAR);
        return String.valueOf(yearEnd - yearInit);
    }

    private File createImageFile(String mCurrentPhotoPath, Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath.concat(image.getAbsolutePath());
        return image;
    }

    public static String getPhotoName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName + ".jpg";
    }

    public static void loadRoundPhoto(Context context, final ImageView imageView, final Resources resources, File file) {
        Picasso.with(context).load(file).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap source = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                RoundedBitmapDrawable drawable =
                        RoundedBitmapDrawableFactory.create(resources, source);
                drawable.setCircular(true);
                drawable.setCornerRadius(Math.max(source.getWidth() / 2.0f, source.getHeight() / 2.0f));
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onError() {

            }
        });
    }

    public static void loadRoundPhoto(Context context, final ImageView imageView, final Resources resources, String url) {
        Picasso.with(context).load(url).fit().into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                Bitmap source = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                RoundedBitmapDrawable drawable =
                        RoundedBitmapDrawableFactory.create(resources, source);
                drawable.setCircular(true);
                drawable.setCornerRadius(Math.max(source.getWidth() / 2.0f, source.getHeight() / 2.0f));
                imageView.setImageDrawable(drawable);
            }

            @Override
            public void onError() {

            }
        });
    }

    public static String formatString(String source) {
        StringBuilder sb = new StringBuilder();
        sb.append(source.charAt(0));
        sb.append(source.substring(1));
        return sb.toString();
    }

    public static void uploadPhoto(final Activity activity, final OnImageUploaded callback, final Report report, final String key) {
        Toast.makeText(activity, "Uploading...", Toast.LENGTH_SHORT).show();
        String uri = report.getEvidence().get("img1").toString();
        // Upload to Cloud Storage
        String uuid = UUID.randomUUID().toString();
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);
        mImageRef.putFile(Uri.parse(uri))
                .addOnSuccessListener(activity, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //noinspection LogConditional
                        String path = taskSnapshot.getMetadata().getReference().getPath();
                        Log.d(TAG, "uploadPhoto:onSuccess:" + path);
                        if (taskSnapshot.getDownloadUrl() != null) {
                            callback.onImageUploaded(taskSnapshot.getDownloadUrl().toString(), report, key);
                        }
                    }
                })
                .addOnFailureListener(activity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "uploadPhoto:onError", e);
                        Toast.makeText(activity, "Upload failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
