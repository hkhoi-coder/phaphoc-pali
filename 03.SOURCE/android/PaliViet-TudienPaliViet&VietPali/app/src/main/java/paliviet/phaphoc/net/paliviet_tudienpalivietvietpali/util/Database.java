package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.model.Term;

/**
 * Created by huytr on 25-03-2016.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DEBUG = "Database";

    //  TODO    Database Version
    public static final int VERSION = 1;
    //  TODO    Database Name
    public static final String NAME = "database.sqlite";

    //  TODO    Helper instance
    private static Database instance;

    //  TODO    Database instance
    private static SQLiteDatabase database;

    private static final String[] dictionaries = {
            "zpaliviet1",
            "zvietpali"
    };

    private Database(Context context) {
        super(context.getApplicationContext() , NAME , null , VERSION);
        //  TODO    Initialize database file
        copyDatabase(context);
    }

    private void copyDatabase(Context context) {
        String databasePath = Storage.help(context).getDatabasePath(NAME);
        if (Storage.help(context).exists(databasePath)) {
            Log.d(DEBUG, "database already exists");
        }
        else {
            try {
                Storage.help(context).copyAsset(NAME , databasePath);
            }
            catch (Exception exception) {
                Log.d(DEBUG , "" + exception.getMessage());
            }
        }
    }

    public static synchronized Database help(Context context) {
        if (instance == null)
            instance = new Database(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  TODO    Initialize database on 1st time
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion <= oldVersion) {
            Log.d(DEBUG, "SQLite database is up-to-date");
            return;
        }
        Log.d(DEBUG, "SQLite database is outdated. (" + oldVersion + " < " + newVersion + ")");
        //  TODO    Upgrade database
        Log.d(DEBUG, "SQLite database it upgraded to " + newVersion);
    }


    public List<String> retrieveTerms(int mode) throws IOException {
        database = getReadableDatabase();

        List<String> result = new ArrayList<>();
        String[] columns = {"zword"};
        Cursor cursor = database.query(dictionaries[mode] , columns , null , null , null , null , null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        //database.close();
        return result;
    }


    public List<String> retrieveFavoriteTerms(List<Integer> integers) throws IOException {
        database = getReadableDatabase();

        List<String> result = new ArrayList<>();
        String[] columns = {"zword" , "zid_dic"};
        Cursor cursor = database.query("zfavorite" , columns , null , null , null , null , null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
                integers.add(cursor.getInt(1));
            } while (cursor.moveToNext());
        }

        //database.close();
        return result;
    }

    public Term retrieveTerm(String key , int mode) throws IOException {
        database = getReadableDatabase();
        Term result = new Term();

        Cursor cursor = database.query(dictionaries[mode] , null , "\"zword\" = \"" + key + "\"", null , null , null , null);

        if (cursor.moveToFirst()) {
            result.setDefinition(cursor.getString(5));
            result.setSource(cursor.getString(6));
            result.setKey(cursor.getString(7));

            boolean isFavorite = true;

            String favoriteText = cursor.getString(3);

            if (favoriteText == null) {
                isFavorite = false;
            }

            result.setFavorite(isFavorite);
        }

        //database.close();
        return result;
    }

    public String retrieveNote(String term) {
        String result = "";
        try {
            database = getReadableDatabase();

            String[] columns = {"znote"};
            Cursor cursor = database.query("zfavorite" , columns , "\"zword\" = \"" +  term + "\"", null , null , null , null);

            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }

            //database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveFavorite(String term, String message , int mode) {
        try {
            database = getWritableDatabase();
            ContentValues updateTermValues = new ContentValues();
            updateTermValues.put("zisfavorite", 1);
            database.update(dictionaries[mode], updateTermValues, "\"zword\" = \"" + term + "\"", null);
            ContentValues updateFavoriteValues = new ContentValues();
            updateFavoriteValues.put("znote", message);
            if (0 == database.update("zfavorite" , updateFavoriteValues , "\"zword\" = \"" + term  + "\"", null))
            {
                ContentValues insertValues = new ContentValues();
                insertValues.put("zword" , term);
                insertValues.put("znote" , message);
                insertValues.put("zid_dic" , mode);
                database.insert("zfavorite" , null , insertValues);
            }
            //database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteFavorite(String term, int mode) {
        try {
            database = getWritableDatabase();

            ContentValues updateTermValues = new ContentValues();
            updateTermValues.put("zisfavorite", 0);
            database.update(dictionaries[mode], updateTermValues, "\"zword\" = \"" + term + "\"", null);
            database.delete("zfavorite", "'zword' = '" + term + "'", null);
            //database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> queryHistory(List<Integer> integers) {
        List<String> results = new ArrayList<>();
        try {
            database = getReadableDatabase();

            String query = "SELECT \"zviewed_date\" , \"zword\" , \"zid_dic\" FROM \"ZHISTORY\" ORDER BY date(\"zviewed_date\") LIMIT 100";
            //String[] columns = {"zviewed_date" , "zword" , "zid_dic"};
            Cursor cursor = database.rawQuery(query , null);
            if (cursor.moveToFirst())
            {
                do {
                    results.add(cursor.getString(1));
                    integers.add(cursor.getInt(2));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return results;
    }

    public void insertHistory(String term , int mode) {
        try {
            database = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("zword" , term);
            values.put("zid_dic" , mode);
            values.put("zviewed_date", " time(\"now\") ");
            database.insert("zhistory", null, values);
        }
        catch (Exception e)
        {

        }
    }
}