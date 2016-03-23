package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;
import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.dao.DictionaryDao;

public class ArchiveActivity extends BaseActivity{

    private ListView mWordList;

    private List<String> mTerms;

    private DictionaryDao mDictionaryDao;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        initComponents();
        inflateContents();
    }

    private void inflateContents() {
        if (getIntent().getBooleanExtra(MainActivity.PASS_TAG_FAVORITE, true)) {
            inflateFavorite();
        } else {
            inflateHistory();
        }
    }

    private void inflateHistory() {

    }

    private void inflateFavorite() {
    }

    private void initComponents() {
        mWordList = (ListView) findViewById(R.id.archiveActivity_listView_list);
        mDictionaryDao = new DictionaryDao(this, MainActivity.DATABASE_VIET);
        try {
            mTerms = mDictionaryDao.retrieveFavoriteTerms();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.ARCHIVE;
    }
}
