package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContents();

        mTerm = getIntent().getStringExtra(MainActivity.TERM);

        try {
            retrieveTermFromId();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Co loi, vui long thu lai sau", Toast.LENGTH_SHORT).show();
            Log.d(LOG_CAT, e.getMessage());
        }

        inflateContents();
    }

    private void initContents() {
        mKey = (TextView) findViewById(R.id.activityDefinition_textView_key);
        mDefinition = (TextView) findViewById(R.id.activityDefinition_textView_meaning);
        mSource = (TextView) findViewById(R.id.activityDefinition_textView_source);
        mButtonSaved = (Button) findViewById(R.id.activityDefinition_button_favorite);
        mDictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
    }

    private void inflateContents() {
        mKey.setText(mCurrentTerm.getKey());
        mDefinition.setText(mCurrentTerm.getDefinition());
        mSource.setText(mCurrentTerm.getSource());

        if (mCurrentTerm.isFavorite()) {
            mButtonSaved.setText("YOLO");
        }
        mButtonSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDictionaryDao.turnOnFavorite(mTerm);
            }
        });
    }

    private void retrieveTermFromId() throws IOException {
        DictionaryDao dictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
        mCurrentTerm = dictionaryDao.retrieveTerm(mTerm);
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.WORD;
    }
}
