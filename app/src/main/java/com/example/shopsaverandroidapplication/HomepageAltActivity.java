package com.example.shopsaverandroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.navigation.NavigationBarView;

public class HomepageAltActivity extends AppCompatActivity {
    private NavigationBarView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Remove title and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_homepage_alt);

        bottomNavigationView = findViewById(R.id.bottomNavBar_main);
        bottomNavigationView.setOnItemSelectedListener(bottomNavMethod);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ListFragment()).commit();
    }

    private NavigationBarView.OnItemSelectedListener bottomNavMethod = new NavigationBarView.OnItemSelectedListener() {

        /**
         * Called when an item in the navigation menu is selected.
         *
         * @param item The selected item
         * @return true to display the item as the selected item and false if the item should not be
         * selected. Consider setting non-selectable items as disabled preemptively to make them
         * appear non-interactive.
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean isSearch = false;
            Fragment fragment = null;
            switch(item.getItemId()) {
                case R.id.home:
                    fragment = new HomeFragment();
                    break;
                case R.id.list:
                    fragment = new ListFragment();
                    break;
                case R.id.tbd:
                    fragment = new TbdFragment();
                    break;
                case R.id.settings:
                    fragment = new SettingsFragment();
                    break;
                case R.id.search:
                    isSearch = true;
                    break;
                default:
                    fragment = null;
            }

            if (isSearch) {
                startActivity(new Intent(HomepageAltActivity.this,
                        SearchActivity.class));
                isSearch = false;
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment).commit();
            }
            return true;
        }
    };
}