package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.Intent;
import android.os.Bundle;
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
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.dao.DictionaryDao;

public class MainActivity extends BaseActivity {

    public static final String DATABASE_VIET = "database.sqlite";

    private static final String LOG_TAG = "debug:MainActivity";

    public static final String TERM = "term";

    private EditText mFilter;

    private ListView mWords;

    private ArrayAdapter mArrayAdapter;

    /* DUMMY WORDS PACK */
    private List<String> mTermsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();

        try {
            loadDatabase();
        } catch (IOException e) {
            Toast.makeText(this, "Co loi xay ra, vui long thu lai sau :(", Toast.LENGTH_SHORT).show();
            Log.d(LOG_TAG, e.getMessage());
        }
        setUpListView();
        setUpFilter();
    }

    private void loadDatabase() throws IOException {
        DictionaryDao dictionaryDao = new DictionaryDao(this, DATABASE_VIET);
        mTermsList = dictionaryDao.retrieveTerms();
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
                Toast.makeText(getApplicationContext(), key, Toast.LENGTH_SHORT).show();
                intent.putExtra(TERM, key);
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
}
