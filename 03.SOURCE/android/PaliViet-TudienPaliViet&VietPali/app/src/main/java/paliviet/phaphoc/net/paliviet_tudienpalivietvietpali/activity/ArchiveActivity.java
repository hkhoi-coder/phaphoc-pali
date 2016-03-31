package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.dao.DictionaryDao;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.util.Database;

public class ArchiveActivity extends BaseActivity {

    public static final String MODE = "MODE";

    private ListView mWordList;

    private List<String> mTerms;

    private List<Integer> integers = new ArrayList<>();

    private int mode = 0;

    //private DictionaryDao mDictionaryDao;

    private ArrayAdapter mArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initComponents();
        inflateContents();

        mode = getIntent().getIntExtra(MODE , 0);
        if (mode == 0)
        {
            navigationView.getMenu().findItem(R.id.favorite).setChecked(true);
            title.setText(R.string.favorite);
        }
        else
        {
            navigationView.getMenu().findItem(R.id.history).setChecked(true);
            title.setText(R.string.history);
        }

    }

    private void inflateContents() {
        if (getIntent().getBooleanExtra(MainActivity.PASS_TAG_FAVORITE, true)) {
            inflateFavorite();
        } else {
            inflateHistory();
        }
    }

    private void inflateHistory() {
        mArrayAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_expandable_list_item_1,
                        android.R.id.text1, mTerms);
        mWordList.setAdapter(mArrayAdapter);
        mWordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DefinitionActivity.class);
                String key = (String) ((TextView) view).getText();
                intent.putExtra(MainActivity.TERM, key);
                intent.putExtra(MainActivity.MODE , integers.get(position));
                startActivity(intent);
            }
        });
    }

    private void inflateFavorite() {
        mArrayAdapter =
                new ArrayAdapter<>(this,
                        android.R.layout.simple_expandable_list_item_1,
                        android.R.id.text1, mTerms);
        mWordList.setAdapter(mArrayAdapter);
        mWordList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DefinitionActivity.class);
                String key = (String) ((TextView) view).getText();
                intent.putExtra(MainActivity.TERM, key);
                intent.putExtra(MainActivity.MODE , integers.get(position));
                startActivity(intent);
            }
        });
    }

    private void initComponents() {
        mWordList = (ListView) findViewById(R.id.archiveActivity_listView_list);
        //mDictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
        try {
            mTerms =  Database.help(getApplicationContext()).retrieveFavoriteTerms(integers);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.ARCHIVE;
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mode == 0)
                mTerms = Database.help(getApplicationContext()).retrieveFavoriteTerms(integers);
            else
                mTerms = Database.help(getApplicationContext()).queryHistory(integers);

            for (String it : mTerms) {
                Log.d("debug", "--- mTerm: " + it);
            }

            mArrayAdapter.clear();
            mArrayAdapter.addAll(mTerms);
            mArrayAdapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
