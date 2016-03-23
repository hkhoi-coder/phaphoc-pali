package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;

public class ArchiveActivity extends BaseActivity{

    private ListView mWordList;

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
    }

    @Override
    protected ActivityType getType() {
        return ActivityType.ARCHIVE;
    }
}
