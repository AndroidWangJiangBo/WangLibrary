package com.android.mylibrary.interfaces;

import java.io.File;

public interface CarmerCallBack {
    void pictureOnGranted(File cameraFile);

    void pictureOnDenied(String permission);
}
