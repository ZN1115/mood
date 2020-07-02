package com.example.zn.emotion_awareness2;

import android.app.Fragment;
import android.app.ListFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.zn.emotion_awareness2.urlhttp.CallBackUtil;
import com.example.zn.emotion_awareness2.urlhttp.UrlHttpUtil;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        BottomNavigationView bottomNav=(BottomNavigationView)findViewById(R.id.bottom_navigation);
        HomeFragment selected=null;
        selected=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener=
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    HomeFragment selectedFragment=null;
                    LocationFragment selectedFragment2=null;
                    ChartFragment selectedFragment3=null;

                    switch (item.getItemId()){
                        case R.id.nav_home:
                            selectedFragment= new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                            break;
                        case R.id.nav_location:
                            selectedFragment2= new LocationFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment2).commit();
                            break;
                        case R.id.nav_chart:
                            selectedFragment3= new ChartFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment3).commit();
                            break;
                    }

                    return true;
                }
            };


}
