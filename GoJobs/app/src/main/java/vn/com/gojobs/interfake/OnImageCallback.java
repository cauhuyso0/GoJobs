package vn.com.gojobs.interfake;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface OnImageCallback {
    void takePicture();

    void takeGallery();

    void onImageCallback(int requestCode, int resultCode, @Nullable Intent data);

    void onRemove();
}
