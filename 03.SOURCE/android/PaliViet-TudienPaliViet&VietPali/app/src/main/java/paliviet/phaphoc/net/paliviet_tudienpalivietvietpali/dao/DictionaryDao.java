package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.model.Term;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util.DatabaseOpener;

/**
 * Created by hkhoi on 3/21/16.
 */
public class DictionaryDao extends DatabaseOpener {

    private static final String RETRIEVE_TERMS = "select zword from zpaliviet1";

    private static final String RETRIEVE_FAVORITE_TERMS = "select zword from zpaliviet1";

    private static final String RETRIEVE_TERM_FROM_ID = "select * from zpaliviet1 where zword = ?";

    private static final String SET_FAVORITE_ON = "update zpaliviet1 set zisfavorite = 1 where zword = ?";

    private static final String SET_FAVORITE_OFF = "update zpaliviet1 set zisfavorite = null where zword = ?";

    private static final String RETRIEVE_NOTE = "select znote from zfavorite where zword = ?";

    private static final String SET_NOTE = "insert into zfavorite (zword, znote) values(?, ?)";

    private static final String REMOVE_NOTE = "delete from zfavorite where zword = ?";

    /**
     * Default flag for database is: OPEN_READWRITE.
     *
     * @param context
     * @param name
     */
    public DictionaryDao(Context context, String name) {
        super(context, name);
    }

    public List<String> retrieveTerms() throws IOException {
        SQLiteDatabase database = openDatabase();

        List<String> result = new ArrayList<>();
        Cursor cursor = database.rawQuery(RETRIEVE_TERMS, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        database.close();
        return result;
    }

    public List<String> retrieveFavoriteTerms() throws IOException {
        SQLiteDatabase database = openDatabase();

        List<String> result = new ArrayList<>();
        Cursor cursor = database.rawQuery(RETRIEVE_FAVORITE_TERMS, null);

        if (cursor.moveToFirst()) {
            do {
                result.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        database.close();
        return result;
    }

    public Term retrieveTerm(String key) throws IOException {
        SQLiteDatabase database = openDatabase();
        Term result = new Term();

        String[] args = {key};
        Cursor cursor = database.rawQuery(RETRIEVE_TERM_FROM_ID, args);

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

        database.close();
        return result;
    }

    public String retrieveNote(String term) {
        String result = "";
        try {
            SQLiteDatabase database = openDatabase();

            String[] args = {term};
            Cursor cursor = database.rawQuery(RETRIEVE_NOTE, args);

            if (cursor.moveToFirst()) {
                result = cursor.getString(0);
            }

            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void saveFavorite(String term, String message) {
        try {
            SQLiteDatabase database = openDatabase();

            String[] args = {term, message};
            String[] arg = {term};
            database.execSQL(SET_FAVORITE_ON, arg);
            database.execSQL(SET_NOTE, args);

            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteFavorite(String term) {
        try {
            SQLiteDatabase database = openDatabase();

            String[] arg = {term};
            database.execSQL(SET_FAVORITE_OFF, arg);
            database.execSQL(REMOVE_NOTE, arg);

            database.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
