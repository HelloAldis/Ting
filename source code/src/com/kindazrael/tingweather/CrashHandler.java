/*
 * Copyright 2014 Augmentum Inc. All rights reserved.
 */

package com.kindazrael.tingweather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.kindazrael.tingweather.util.LogUtil;

public class CrashHandler implements UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getName();
    private static CrashHandler iWCrashHandler = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";

    private static final String CRASH_REPORTER_EXTENSION = ".cr";

    private CrashHandler () {

    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static CrashHandler getInstance() {
        return iWCrashHandler;
    }

    @ Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LogUtil.e(TAG, "error : ", e);
            }

            // Intent intent = new Intent();
            // intent.setClass(mContext,MainActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // mContext.startActivity(intent);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }

    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        new Thread() {

            @ Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "很抱歉，程序出现异常，即将退出.", Toast.LENGTH_LONG)
                        .show();
                Looper.loop();
            }
        }.start();

        collectCrashDeviceInfo(mContext);
        saveCrashInfoToFile(ex);

        sendCrashReportsToServer(mContext);

        return true;
    }

    private String [] getCrashReportFiles(Context ctx) {
        File filesDir = ctx.getFilesDir();
        FilenameFilter filter = new FilenameFilter() {

            @ Override
            public boolean accept(File dir, String name) {
                return name.endsWith(CRASH_REPORTER_EXTENSION);
            }
        };
        return filesDir.list(filter);
    }

    private void sendCrashReportsToServer(Context ctx) {
        String [] crFiles = getCrashReportFiles(ctx);
        Map <String, Object> files = new HashMap <String, Object>();
        if (crFiles != null && crFiles.length > 0) {
            TreeSet <String> sortedFiles = new TreeSet <String>();
            sortedFiles.addAll(Arrays.asList(crFiles));

            for (String fileName : sortedFiles) {
                File cr = new File(ctx.getFilesDir(), fileName);
                files.put(fileName, cr);
            }
        }

        // call HttpImpl.postWithFile

        Set <String> keys = files.keySet();
        for (String key : keys) {
            Object object = files.get(key);
            if (object instanceof File) {
                File file = (File) object;
                file.delete();
            }
        }

    }

    public void collectCrashDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                mDeviceCrashInfo.put(VERSION_NAME,
                        pi.versionName == null ? "not set" : pi.versionName);
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
            }
        } catch (NameNotFoundException e) {
            LogUtil.e(TAG, "Error while collect package info", e);
        }

        Field [] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                mDeviceCrashInfo.put(field.getName(), field.get(null));
                LogUtil.d(TAG, field.getName() + " : " + field.get(null));

            } catch (Exception e) {
                LogUtil.e(TAG, "Error while collect crash info", e);
            }
        }
    }

    private String saveCrashInfoToFile(Throwable ex) {
        Writer info = new StringWriter();
        PrintWriter printWriter = new PrintWriter(info);
        ex.printStackTrace(printWriter);

        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }

        String result = info.toString();
        printWriter.close();
        mDeviceCrashInfo.put(STACK_TRACE, result);
        System.out.println(result);

        try {
            long timestamp = System.currentTimeMillis();
            String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
            FileOutputStream trace = mContext.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            mDeviceCrashInfo.store(trace, "");
            trace.flush();
            trace.close();
            return fileName;
        } catch (Exception e) {
            LogUtil.e(TAG, "an error occured while writing report file...", e);
        }
        return null;
    }

}
