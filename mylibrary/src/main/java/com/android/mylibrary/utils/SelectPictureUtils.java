package com.android.mylibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.widget.Toast;

import com.android.mylibrary.interfaces.CarmerCallBack;
import com.android.mylibrary.base.BaseActivity;
import com.android.mylibrary.interfaces.PictureCallBack;
import com.android.mylibrary.runtimepermissions.PermissionsManager;
import com.android.mylibrary.runtimepermissions.PermissionsResultAction;

import java.io.File;
import java.io.IOException;

public class SelectPictureUtils {

    public static final int REQUEST_CODE_CAMERA = 1000;
    public static final int REQUEST_CODE_LOCAL = 1001;

    /**
     * 选择图片
     * 在activity中重写onRequestPermissionsResult
     *
     * @param activity
     */
    public static void selectPicFromLocal(final BaseActivity activity) {
        String[] pers = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, pers, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                }
                activity.startActivityForResult(intent, REQUEST_CODE_LOCAL);
            }

            @Override
            public void onDenied(String permission) {
            }
        });
    }

    /**
     * 拍照
     * 在activity中重写onRequestPermissionsResult
     *
     * @param activity
     * @param cameraFile 可以为空
     * @param callBack
     */
    public static void selectPicFromCamera(final BaseActivity activity, File cameraFile, final CarmerCallBack callBack) {
        if (!isSdcardExist()) {
            Toast.makeText(activity, "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
            return;
        }

        if (cameraFile == null) {
            cameraFile = new File(activity.getExternalCacheDir() + String.valueOf(System.currentTimeMillis()) + "temp.jpg");
        }

        final File finalCameraFile = cameraFile;

        String[] pers = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, pers, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                try {
                    if (finalCameraFile.exists()) {
                        finalCameraFile.delete();
                    }
                    finalCameraFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                activity.startActivityForResult(
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(activity, finalCameraFile)),
                        REQUEST_CODE_CAMERA);
                if (callBack != null) {
                    callBack.pictureOnGranted(finalCameraFile);
                }

            }

            @Override
            public void onDenied(String permission) {
                if (callBack != null) {
                    callBack.pictureOnDenied(permission);
                }
                Toast.makeText(activity, "SD卡不存在，不能拍照", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Uri getUriForFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * check if sdcard exist
     *
     * @return
     */
    public static boolean isSdcardExist() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * @param activity
     * @param selectedImage onActivityResult返回值
     */
    private static void sendPicByUri(final BaseActivity activity, Uri selectedImage, PictureCallBack callBack) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(activity, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            callBack.pictureOnGranted(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(activity, "找不到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            callBack.pictureOnGranted(file.getAbsolutePath());
        }

    }

    public static void onActivityResult(final BaseActivity activity, int requestCode, int resultCode, Intent data, File cameraFile, final CarmerCallBack carmerCallBack, PictureCallBack pictureCallBack) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists()) {
                    carmerCallBack.pictureOnGranted(cameraFile);
                } else {
                    carmerCallBack.pictureOnDenied("文件不存在");
                }

            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(activity, selectedImage, pictureCallBack);
                    } else {
                        pictureCallBack.pictureOnGranted("");
                    }
                } else {
                    pictureCallBack.pictureOnGranted("");
                }
            }
        }
    }


}
