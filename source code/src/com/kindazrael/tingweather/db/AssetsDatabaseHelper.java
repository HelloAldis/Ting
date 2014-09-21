
package com.kindazrael.tingweather.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.kindazrael.tingweather.IWApplication;
import com.kindazrael.tingweather.util.LogUtil;

public class AssetsDatabaseHelper {

    private static final String TAG = "AssetsDatabaseHelper";
    private static String DB_PATH = "/data/data/%s/database";
    private static String DB_NAME = "iWeatherDB.s3db";
    private static String ASSETS_NAME = "iWeatherDB.s3db";
    private static AssetsDatabaseHelper instance = null;
    private SQLiteDatabase sqliteDatabase = null;
    private Context mContext = null;

    public AssetsDatabaseHelper (Context context) {
        this.mContext = context;
    }

    public SQLiteDatabase createDataBase() {
        String mPath = DB_PATH + DB_NAME;
        try {
            File file = new File(mPath);
            if (file.exists()) {
                sqliteDatabase = SQLiteDatabase.openDatabase(mPath, null,
                        SQLiteDatabase.OPEN_READONLY);
            }
        } catch (SQLiteException e) {
            // database does't exist yet.
            LogUtil.e(TAG, "Dataabse not exits");
        }
        if (null != sqliteDatabase) {
            // 数据库已存在，do nothing.
        } else {
            // 创建数据库
            try {
                File dir = new File(DB_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File dbfile = new File(mPath);
                if (!dbfile.exists()) {
                    copyDataBase();
                }
                sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(dbfile,
                        null);
            } catch (Exception e) {
                LogUtil.e(TAG, "Create database error");
            }
        }
        return sqliteDatabase;
    }

    public void closeDatabase() {
        if (null != sqliteDatabase) {
            sqliteDatabase.close();
            sqliteDatabase = null;
        }
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            // Open your local db as the input stream
            inputStream = mContext.getAssets().open(ASSETS_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            // Open the empty db as the output stream
            outputStream = new FileOutputStream(outFileName);
            // transfer bytes from the inputfile to the outputfile
            byte [] buffer = new byte [1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            // Close the streams
            outputStream.flush();
        } catch (Exception e) {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    public static AssetsDatabaseHelper getInstance() {
        if (null == instance) {
            instance = new AssetsDatabaseHelper(IWApplication.getInstance());
            DB_PATH = String.format(DB_PATH, IWApplication.getInstance()
                    .getPackageName());
        }
        return instance;
    }
}
