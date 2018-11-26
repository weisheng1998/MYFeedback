package com.myfeedback.myfeedbackprototype;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView text1;
    private DrawerLayout mDrawerLayout;

    // these two variables will be used by SharedPreferences
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //initialisation
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main);

        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //convert to actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (menuItem.getItemId()) {
                            case R.id.login:
                                break;

                            case R.id.profile:
                                break;

                            case R.id.aboutus:
                                break;

                            case R.id.exit:
                                onBackPressed();
                                break;
                        }

                        return true;
                    }
                });

        //testing textview while navigating
        text1 = findViewById(R.id.text1);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.news:
                                text1.setText(R.string.tabLabel1);
                                break;

                            case R.id.complaint:
                                text1.setText(R.string.tabLabel2);
                                break;

                            case R.id.achievement:
                                text1.setText(R.string.tabLabel3);
                                break;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //exit program
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage(R.string.exitPrompt)
                .setCancelable(false)
                .setPositiveButton( R.string.Yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    public void chgDialogList(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose your preferred language.");

        String[] lang = {"English", "中文", "Bahasa Malaysia", "Tamil"};
        builder.setItems(lang, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        changeLang("en");
                        break;
                    case 1:
                        changeLang("zh");
                        break;
                    case 2:
                        changeLang("ms");
                        break;
                    case 3:
                        changeLang("ta");
                        break;
                }
            }
        });

    // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //change language code
    private void changeLang(String lang) {
        SharedPreferences preferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_LANG, lang);
        editor.apply();
        // recreate activity after saving to load the new language, this is the same
        // as refreshing activity to load new language
        recreate();
    }

    private void loadLanguage() {
        // we can use this method to load language,
        // this method should be called before setContentView() method of the onCreate method
        Locale locale = new Locale(getLangCode());
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private String getLangCode() {
        SharedPreferences preferences = getSharedPreferences(FILE_NAME, MODE_PRIVATE);
        String langCode = preferences.getString(KEY_LANG, "en");
        return langCode;
    }
}
