package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.model.Term;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util.Database;

public class DefinitionActivity extends BaseActivity {

    private static final String LOG_CAT = "debug:Defninition";

    private String mTerm;
    private String note;

    private Term mCurrentTerm;

    private TextView mKey;

    private TextView mTitle;

    private TextView mDefinition;

    private TextView mSource;

    private ImageButton mButtonSaved;

    int mode;

    //private DictionaryDao mDictionaryDao;

    private EditText mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContents();
        mTerm = getIntent().getStringExtra(MainActivity.TERM);
        mode = getIntent().getIntExtra(MainActivity.MODE , 0);
        retrieveTermFromId();
        inflateContents();
        setUpSaveButton();
    }

    private void setUpSaveButton() {
        mButtonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentTerm.setFavorite(!mCurrentTerm.isFavorite());
                if (mCurrentTerm.isFavorite()) {
                    mButtonSaved.setImageResource(R.drawable.star_solid);
                    mNote.setVisibility(View.VISIBLE);
                } else {
                    mButtonSaved.setImageResource(R.drawable.star_empty);
                    mNote.setVisibility(View.GONE);
                    Database.help(getApplicationContext()).deleteFavorite(mTerm, mode);
//                    mDictionaryDao.deleteFavorite(mTerm);
                }
            }
        });
    }

    private void initContents() {
        mKey = (TextView) findViewById(R.id.activityDefinition_textView_key);
        mDefinition = (TextView) findViewById(R.id.activityDefinition_textView_meaning);
        mSource = (TextView) findViewById(R.id.activityDefinition_textView_source);
        mButtonSaved = (ImageButton) findViewById(R.id.activityDefinition_button_favorite);
        //mDictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
        mNote = (EditText) findViewById(R.id.activityMain_editText_note);
        mTitle = (TextView) findViewById(R.id.baseActivity_textView_title);
    }

    private void inflateContents() {
        mTitle.setText(mTerm);
        mKey.setText(mCurrentTerm.getKey());
        mDefinition.setText(mCurrentTerm.getDefinition());
        mSource.setText(mCurrentTerm.getSource());
        mNote.setText(note);

        if (mCurrentTerm.isFavorite()) {
            mButtonSaved.setImageResource(R.drawable.star_solid);
            retrieveAndShowNote();
        }
    }

    private void retrieveAndShowNote() {
        String note = Database.help(getApplicationContext()).retrieveNote(mTerm);
        mNote.setVisibility(View.VISIBLE);
        mNote.setText(note);
    }

    private void retrieveTermFromId() {
        try {
            mCurrentTerm = Database.help(getApplicationContext()).retrieveTerm(mTerm , mode);
            note = Database.help(getApplicationContext()).retrieveNote(mCurrentTerm.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.WORD;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCurrentTerm.isFavorite()) {
            Database.help(getApplicationContext()).saveFavorite(mTerm, mNote.getText().toString() , mode);
        }
    }
}
