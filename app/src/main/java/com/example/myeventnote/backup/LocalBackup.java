package com.example.myeventnote.backup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.myeventnote.R;
import com.example.myeventnote.activities.MainActivity;
import com.example.myeventnote.helpers.MySQLiteOpenHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;

import java.io.File;
import java.security.Permissions;

public class LocalBackup {
    private MainActivity activity;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public LocalBackup(MainActivity activity) {
        this.activity = activity;
    }
    // 命名的備份檔案會儲存於建立的資料夾底下
    public void performBackup(final MySQLiteOpenHelper helper, final String outFileName) {
        verifyStoragePermissions(activity);
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + activity.getResources().getString(R.string.app_name));
        //Log.d("folder", "folder path:" + Environment.getExternalStorageDirectory().getPath() + File.separator + activity.getResources().getString(R.string.app_name));
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            Toast.makeText(activity, "已建立MyEventNote資料夾", Toast.LENGTH_SHORT).show();
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity);
            builder.setTitle("備份檔案名稱");
            final EditText input = new EditText(activity);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String m_Text = input.getText().toString();
                    String out = outFileName + m_Text + ".db";
                    helper.exportDB(out);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
        else {
            Toast.makeText(activity, "無法建立資料夾! 請重試", Toast.LENGTH_SHORT).show();
        }
    }
    // 顯示可以選擇匯入的檔案
    public void performRestore(final MySQLiteOpenHelper helper) {
        verifyStoragePermissions(activity);
        File folder = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + activity.getResources().getString(R.string.app_name));
        if (folder.exists()) {
            final File[] files = folder.listFiles();  // 檔案列表
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, android.R.layout.select_dialog_item);
            if (files != null) {
                for (File file : files) {
                    arrayAdapter.add(file.getName());
                }
                MaterialAlertDialogBuilder builderSingle = new MaterialAlertDialogBuilder(activity);
                builderSingle.setTitle("檔案匯入");
                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            helper.importDB(files[which].getPath());
                            activity.refresh();
                        } catch (Exception e) {
                            Toast.makeText(activity, "無法匯入! 請重試", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builderSingle.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.show();
            }
            else {
                Toast.makeText(activity, "沒有檔案.\n請備份再匯入!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(activity, "沒有資料夾.\n請備份再匯入!", Toast.LENGTH_SHORT).show();
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // 檢查寫入和讀取權限
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // 要求給予權限
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
    }
}