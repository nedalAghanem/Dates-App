package com.example.dates.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dates.Adapters.*;
import com.example.dates.databinding.ActivityHomeBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding b;
    PageAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        b.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), NewAppointmentActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTabs();

    }

    private void setTabs() {
        pagerAdapter = new PageAdapter(getSupportFragmentManager());
        b.viewPager.setAdapter(pagerAdapter);
        b.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(b.tabLayout));
        b.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(b.viewPager));
    }

}