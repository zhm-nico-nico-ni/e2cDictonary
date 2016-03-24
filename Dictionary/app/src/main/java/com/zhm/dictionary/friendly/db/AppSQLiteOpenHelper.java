package com.zhm.dictionary.friendly.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppSQLiteOpenHelper extends SQLiteOpenHelper {

    final static int DATABASE_VERSION = 1;

    final static String DATABASE_NAME = "ecdict.db";

    private static String DB_PATH;

    private SQLiteDatabase mDataBase;

    private Context mContext;

    public AppSQLiteOpenHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        mContext = context.getApplicationContext();
    }

    public void createDataBase() throws IOException {

        // If database not exists copy it from the assets

        boolean mDataBaseExist = checkDataBase();

        if (!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assests
                copyDataBase();
                Log.e("DataBaseHelper", "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("Error Copying DataBase");
            }
        }

    }

    /**
     * Called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DATABASE_NAME);
        //Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException {


        InputStream mInput = mContext.getAssets().open(DATABASE_NAME);
        String outFileName = DB_PATH + DATABASE_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();

    }

    public SQLiteDatabase openDataBase() throws SQLException {
        if (mDataBase == null || !mDataBase.isOpen()) {
            // Open the database
            String myPath = DB_PATH + DATABASE_NAME;
            mDataBase = SQLiteDatabase.openDatabase(myPath, null,
                    SQLiteDatabase.OPEN_READONLY);
        }

        return mDataBase;
    }

    @Override
    public synchronized void close() {

        if (mDataBase != null)
            mDataBase.close();

        super.close();

    }

}
