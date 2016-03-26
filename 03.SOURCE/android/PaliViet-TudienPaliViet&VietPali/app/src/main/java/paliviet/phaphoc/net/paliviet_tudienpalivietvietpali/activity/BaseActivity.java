package paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import paliviet.phaphoc.net.paliviet_tudienpalivietvietpali.R;

public abstract class BaseActivity extends AppCompatActivity {

    public static final String PASS_TAG_FAVORITE = "pass_tag";
    private static final boolean PASS_FAVORITE = true;

    protected enum ActivityType {
        MAIN ,
        WORD ,
        ARCHIVE
    };

    protected DrawerLayout drawerLayout;
    protected Toolbar toolbar;
    protected ActionBar actionBar;
    protected NavigationView navigationView;
    protected TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpLayout();

        title = (TextView) findViewById(R.id.baseActivity_textView_title);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.menu_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pali_viet:
                        navigatePaliViet();
                        break;
                    case R.id.viet_pali:
                        navigateVietPali();
                        break;
                    case R.id.favorite:
                        navigateFavorite();
                        break;
                    case R.id.history:
                        navigateHistory();
                        break;
                    case R.id.feedback:
                        navigateFeedback();
                        break;
                    case R.id.rating:
                        navigateRatingIntent();
                        break;
                    case R.id.sharing:
                        navigateSharingIntent();
                        break;
                    case R.id.help:
                        navigateHelpIntent();
                        break;
                }
                return false;
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar ,  R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                /* TODO: Deal with number of learned and hard words */
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
    }

    private void navigateRatingIntent() {
        String link = getResources().getString(R.string.google_play_url) + getPackageName();
        Log.d("debug", "---Link = " + link);
        Uri uri = Uri.parse(link);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setUpLayout() {
        switch (getType()) {
            case MAIN:
                setContentView(R.layout.activity_main);
                break;
            case WORD:
                setContentView(R.layout.activity_definition);
                break;
            case ARCHIVE:
                setContentView(R.layout.activity_archive);
                break;
            default:
                Log.d("debug" , "ERROR - Unknown Activity");
                break;
        }
    }

    protected abstract ActivityType getType();
    private void navigateHelpIntent() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Unknown")
                .setTitle(R.string.help)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    private void navigateSharingIntent() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.sharing_subject));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharing_text));
        try {
            startActivity(Intent.createChooser(sharingIntent, getString(R.string.send_via)));
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void navigateFeedback() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.feeback_email)});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Pali-Viet][Feedback]");
        startActivity(Intent.createChooser(emailIntent, " Gởi phản hồi"));
        try {
            startActivity(Intent.createChooser(emailIntent, getString(R.string.choose_email_client)));
        } catch (android.content.ActivityNotFoundException e) {
        }
    }

    private void navigatePaliViet() {
        Intent paliViet = new Intent(this , MainActivity.class);
        paliViet.putExtra(MainActivity.MODE, 0);
        //paliViet.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(paliViet);
    }

    private void navigateVietPali() {
        Intent vietPali = new Intent(this , MainActivity.class);
        vietPali.putExtra(MainActivity.MODE, 1);
        //vietPali.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(vietPali);
    }

    private void navigateFavorite() {
        Intent intent = new Intent(this, ArchiveActivity.class);
        intent.putExtra(ArchiveActivity.MODE, 0);
        intent.putExtra(PASS_TAG_FAVORITE, PASS_FAVORITE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void navigateHistory() {
        Intent history = new Intent(this , ArchiveActivity.class);
        history.putExtra(ArchiveActivity.MODE, 1);
        //history.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        finish();
        startActivity(history);
    }
}