package com.example.mychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ViewPager viewPager;
    FirebaseDatabase fDb;
    DatabaseReference dbRef;
    MenuItem prevMenuItem;


    public static boolean ONLINE=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.btmNavigation);
        viewPager = findViewById(R.id.pager);

        fDb = FirebaseDatabase.getInstance();
        dbRef = fDb.getReference("users").child(FirebaseAuth.getInstance().getUid());

        BottomPagerAdapter adapter = new BottomPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPagerChangeLister();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    viewPager.setCurrentItem(0);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                    return true;
                } else if (itemId == R.id.people) {
                    viewPager.setCurrentItem(1);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,usersFragment).commit();
                    return true;
                } else if (itemId == R.id.settings) {
                    viewPager.setCurrentItem(2);
//                        getSupportFragmentManager().beginTransaction().replace(R.id.container,settingsFragment).commit();
                    return true;
                }
                return false;
            }
        });
        online();
    }

    private void online() {
        if (!ONLINE) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", "online");
            dbRef.updateChildren(map);
        }
    }

    public void offline() {
        if (ONLINE) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", "offline");
            dbRef.updateChildren(map);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        offline();
    }

    @Override
    protected void onStart() {
        super.onStart();
        online();
    }


    @Override
    protected void onDestroy() {
        offline();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        offline();
    }

    private void viewPagerChangeLister() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static class BottomPagerAdapter extends FragmentPagerAdapter {

        public BottomPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new ChatFragment();
                case 1:
                    return new UsersFragment();
                case 2:
                    return new SettingsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}