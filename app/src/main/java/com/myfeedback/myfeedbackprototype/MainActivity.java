package com.myfeedback.myfeedbackprototype;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //home fragment variables fragmentN => N equals 'N'ews and vice versa
    final Fragment fragmentN = new NewsFragment();
    final Fragment fragmentC = new ComplaintFragment();
    final Fragment fragmentA = new AchievementFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragmentN;

    private DrawerLayout mDrawerLayout;
    TextView tv;

    // these two variables will be used by SharedPreferences
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //initialisation
        super.onCreate(savedInstanceState);

        //make status bar translucent
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(0x00000000);  // transparent
        }
        loadLanguage();

        setContentView(R.layout.activity_main);

        //hide fragment
        BottomNavigationView Bnavigation = findViewById(R.id.bottom_navigation);
        Bnavigation.setOnNavigationItemSelectedListener(mBottomNavigation);

        fm.beginTransaction().add(R.id.homeFragmentPlaceholder, fragmentA, "3").hide(fragmentA).commit();
        fm.beginTransaction().add(R.id.homeFragmentPlaceholder, fragmentC, "2").hide(fragmentC).commit();
        fm.beginTransaction().add(R.id.homeFragmentPlaceholder, fragmentN, "1").commit();

        //add toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //convert to actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        toolbar.setTitleTextAppearance(this, R.style.GoogleSansTextAppearance);

        //Navigation Drawer
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //check weather user has logged in
        View headerView = navigationView.getHeaderView(0);
        tv = headerView.findViewById(R.id.nav_header_textView);
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            tv.setText(SharedPrefManager.getInstance(this).getKeyUserEmail());

            //press profile picture to navigate to profile page
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchProfile();
                }
            });
        } else {
            tv.setText("MYFeedback");
        }

        //link to other activity from the navigation drawer
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        switch (menuItem.getItemId()) {
                            case R.id.login:
                                launchLogin();
                                break;

                            case R.id.register:
                                launchRegister();
                                break;

                            case R.id.logout:
                                SharedPrefManager.getInstance(MainActivity.this).logout();
                                finishAndRemoveTask();
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                startActivity(intent);
                                break;

                            case R.id.profile:
                                launchProfile();
                                break;

                            case R.id.aboutus:
                                launchAboutUs();
                                break;
                        }
                        return true;
                    }
                });
    }

    private void launchAboutUs() {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
    }

    private void launchRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void launchLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void launchProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    //this code will run before the navigation drawer is initialised, so that we can programmatically configure the
    //visibility of the menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu login_menu = navigationView.getMenu();
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            login_menu.findItem(R.id.login).setVisible(false);
            login_menu.findItem(R.id.register).setVisible(false);
            login_menu.findItem(R.id.logout).setVisible(true);
        } else {
            login_menu.findItem(R.id.login).setVisible(true);
            login_menu.findItem(R.id.register).setVisible(true);
            login_menu.findItem(R.id.logout).setVisible(false);
        }
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mBottomNavigation
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.news:
                    fm.beginTransaction().hide(active).show(fragmentN).commit();
                    active = fragmentN;
                    return true;

                case R.id.complaint:
                    fm.beginTransaction().hide(active).show(fragmentC).commit();
                    active = fragmentC;
                    return true;

                case R.id.achievement:
                    fm.beginTransaction().hide(active).show(fragmentA).commit();
                    active = fragmentA;
                    return true;
            }
            return false;
        }
    };

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
    boolean doubleBackToExitPressedOnce = false;
    private ConstraintLayout constLayout;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
        }

        constLayout = findViewById(R.id.mainConstLayout);

        this.doubleBackToExitPressedOnce = true;
        Snackbar snackbar = Snackbar.make(constLayout, R.string.exitPrompt, Snackbar.LENGTH_SHORT);
        snackbar.show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
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
        Intent intent = getIntent();
        finish();
        startActivity(intent);
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
