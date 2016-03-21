package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hkhoi on 1/20/16.
 */
public class DatabaseOpener {

    private Context mContext;
    private String mDatabaseName;
    private String mDatabasePath;
    private SQLiteDatabase mDatabase;

    /**
     * Default flag for database is: OPEN_READWRITE.
     * @param context
     * @param name
     */
    public DatabaseOpener(Context context, String name) {
        mContext = context;
        mDatabaseName = name;
        mDatabasePath = mContext.getDatabasePath(mDatabaseName).getAbsolutePath();
    }

    /**
     * Always call this method before you do something with your database.
     * @throws IOException
     */
    protected SQLiteDatabase openDatabase() throws IOException {
        if (!isExisted()) {
            copyAndLoadDatabase();
            mDatabase = SQLiteDatabase.openDatabase(mDatabasePath, null, SQLiteDatabase.OPEN_READWRITE);
        }
        return mDatabase;
    }

    /**
     * This method should be called after finishing your tasks with database.
     */
    protected void closeDatabase() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    /**
     * In case of the database is not there.
     * @throws IOException
     */
    private void copyAndLoadDatabase() throws IOException {
        InputStream inputStream = mContext.getAssets().open(mDatabaseName);
        File outDirectory = new File(mDatabasePath);
        outDirectory.getParentFile().mkdirs();
        OutputStream outputStream = new FileOutputStream(mDatabasePath);

        byte[] buf = new byte[1024];
        int byteRead;

        while ((byteRead = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, byteRead);
        }
    }

    /**
     * @return true if the database is copied to the internal memory.
     */
    private boolean isExisted() {
        try {
            mDatabase = SQLiteDatabase.openDatabase(mDatabasePath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            // db = null -> db not available
        }
        return mDatabase != null;
    }
}
