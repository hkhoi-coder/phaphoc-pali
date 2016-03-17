package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali;

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected ActivityType getType() {
        return ActivityType.MAIN;
    }
}
