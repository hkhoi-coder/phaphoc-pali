package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.dao.DictionaryDao;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.model.Term;

public class DefinitionActivity extends BaseActivity {

    private static final String LOG_CAT = "debug:Defninition";

    private String mTerm;

    private Term mCurrentTerm;

    private TextView mKey;

    private TextView mDefinition;

    private TextView mSource;

    private Button mButtonSaved;

    private DictionaryDao mDictionaryDao;

    private EditText mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContents();

        mTerm = getIntent().getStringExtra(MainActivity.TERM);

        try {
            retrieveTermFromId();
            inflateContents();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Co loi, vui long thu lai sau", Toast.LENGTH_SHORT).show();
            Log.d(LOG_CAT, e.getMessage());
        }
    }

    private void initContents() {
        mKey = (TextView) findViewById(R.id.activityDefinition_textView_key);
        mDefinition = (TextView) findViewById(R.id.activityDefinition_textView_meaning);
        mSource = (TextView) findViewById(R.id.activityDefinition_textView_source);
        mButtonSaved = (Button) findViewById(R.id.activityDefinition_button_favorite);
        mDictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
        mNote = (EditText) findViewById(R.id.activityMain_editText_note);
    }

    private void inflateContents() throws IOException {
        mKey.setText(mCurrentTerm.getKey());
        mDefinition.setText(mCurrentTerm.getDefinition());
        mSource.setText(mCurrentTerm.getSource());

        if (mCurrentTerm.isFavorite()) {
            mButtonSaved.setText("YOLO");
            retrieveAndShowNote();
        }
        mButtonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    toggleFavorite();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void retrieveAndShowNote() throws IOException {
        String note = mDictionaryDao.retrieveNote(mTerm);
        mNote.setText(note);
        Toast.makeText(getApplicationContext(), note, Toast.LENGTH_SHORT).show();
        mNote.setVisibility(View.VISIBLE);
    }

    private void retrieveTermFromId() throws IOException {
        mCurrentTerm = mDictionaryDao.retrieveTerm(mTerm);
    }

    private void toggleFavorite() throws IOException {
        if (mCurrentTerm.isFavorite()) {
            mButtonSaved.setText("NOT SAVED");
            mCurrentTerm.setFavorite(false);
            mNote.setVisibility(View.GONE);
        } else {
            mButtonSaved.setText("YOLO");
            mCurrentTerm.setFavorite(true);
            mNote.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.WORD;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        if (mCurrentTerm.isFavorite()) {
            try {
                mDictionaryDao.turnOnFavorite(mTerm);
                mDictionaryDao.setNote(mTerm, mNote.getText().toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mDictionaryDao.turnOffFavotite(mTerm);
                mDictionaryDao.removeNote(mTerm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
