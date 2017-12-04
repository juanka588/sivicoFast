package com.rocket.sivico.Interfaces;

import com.rocket.sivico.Data.Report;

/**
 * Created by Camilo on 4/12/2017.
 */

public interface OnImageUploaded {
    void onImageUploaded(String downloadURL, Report report, String key);
}
