package com.rocket.sivico;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by JuanCamilo on 28/09/2017.
 */

public class Utils {
    public static SimpleDateFormat sivicoDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    public static SimpleDateFormat sivicoHourFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
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
}
