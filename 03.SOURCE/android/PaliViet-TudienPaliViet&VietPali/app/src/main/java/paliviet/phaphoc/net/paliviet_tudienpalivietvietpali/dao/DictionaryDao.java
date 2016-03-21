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

    private static final String RETRIEVE_TERM_FROM_ID = "select * from zpaliviet1 where zword = ";

    private static final String SET_FAVORITE_ON = "update zpaliviet1 set zisfavorite = 1 where zword = ";

    private static final String SET_FAVORITE_OFF = "update zpaliviet1 set zisfavorite = null where zword = ";

    private static final String RETRIEVE_NOTE = "select znote from zfavorite where zword = ";

    private static final String SET_NOTE = "update zfavorite set znote = ? where zword = ?";

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

    public Term retrieveTerm(String key) throws IOException {
        SQLiteDatabase database = openDatabase();
        Term result = new Term();

        String script = RETRIEVE_TERM_FROM_ID + '\"' + key + '\"';
        Cursor cursor = database.rawQuery(script, null);

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

    public void turnOnFavorite(String term) throws IOException {
        SQLiteDatabase database = openDatabase();

        String script = SET_FAVORITE_ON + '\"' + term + '\"';
        database.execSQL(script);

        database.close();
    }

    public void turnOffFavotite(String term) throws IOException {
        SQLiteDatabase database = openDatabase();

        String script = SET_FAVORITE_OFF + '\"' + term + '\"';
        database.execSQL(script);

        database.close();
    }

    public String retrieveNote(String term) throws IOException {
        SQLiteDatabase database = openDatabase();

        String result = "";

        String script = RETRIEVE_NOTE + '\"' + term + '\"';
        Cursor cursor = database.rawQuery(script, null);
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }

        database.close();
        return result;
    }

    public void setNote(String term, String note) throws IOException {
        SQLiteDatabase database = openDatabase();

        String[] args = {note, term};
        database.execSQL(SET_NOTE, args);

        database.close();
    }


    public void removeNote(String mTerm) {
    }
}
