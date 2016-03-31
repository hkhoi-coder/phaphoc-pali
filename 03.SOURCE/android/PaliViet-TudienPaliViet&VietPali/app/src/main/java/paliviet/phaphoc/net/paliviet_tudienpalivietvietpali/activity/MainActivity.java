package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util.Database;

public class MainActivity extends BaseActivity {

    public static final String MODE = "mode";

    public static final String DATABASE_VIET = "database.sqlite";

    private static final String LOG_TAG = "debug:MainActivity";

    public static final String TERM = "term";

    private int mode;

    private EditText mFilter;

    private ListView mWords;

    private ArrayAdapter mArrayAdapter;

    //private DictionaryDao mDictionaryDao;

    /* DUMMY WORDS PACK */
    private List<String> mTermsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();

        mode = getIntent().getIntExtra(MODE , 0);
        try {
            loadDatabase();
        } catch (IOException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
        if (mode == 0)
        {
            navigationView.getMenu().findItem(R.id.pali_viet).setChecked(true);
            title.setText(R.string.pali_viet);
        }
        else {
            navigationView.getMenu().findItem(R.id.viet_pali).setChecked(true);
            title.setText(R.string.viet_pali);
        }
        setUpListView();
        setUpFilter();
    }

    private void loadDatabase() throws IOException {
        //mDictionaryDao = new DictionaryDao(this, DATABASE_VIET);
        mTermsList = Database.help(getApplicationContext()).retrieveTerms(mode);
    }

    private void setUpFilter() {
        mFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mArrayAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpListView() {
        mArrayAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_expandable_list_item_1,
                        android.R.id.text1, mTermsList);
        mWords.setAdapter(mArrayAdapter);

        mWords.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DefinitionActivity.class);
                String key = (String) ((TextView) view).getText();
                Database.help(getApplicationContext()).insertHistory(key, mode);
                intent.putExtra(TERM, key);
                intent.putExtra(MODE, mode);
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        mFilter = (EditText) findViewById(R.id.activityMain_editText_filter);
        mWords = (ListView) findViewById(R.id.activityMain_listView_words);
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.MAIN;
    }

    @Override
    public void onBackPressed() {
        if (backPressedOnce) {
            super.onBackPressed();
            info.cancel();
            finish();
        } else {
//            Toast.makeText(getApplication(), R.string.press_once_more, Toast.LENGTH_SHORT).show();
            info.show();
            backPressedOnce = true;
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedOnce = false;
                }
            }, 2000);
        }
    }
}
