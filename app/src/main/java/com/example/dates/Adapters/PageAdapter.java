package com.example.dates.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dates.Fragments.HomeFragment;
import com.example.dates.Fragments.CompanyFragment;
import com.example.dates.Fragments.UserProfileFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private Bundle bundle = new Bundle();

    public PageAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                return homeFragment;
            case 1:
                CompanyFragment companyFragment = new CompanyFragment();
                companyFragment.setArguments(bundle);
                return companyFragment;
            case 2:
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setArguments(bundle);
                return userProfileFragment;
        }
        return fragment;

    }


    @Override
    public int getCount() {
        return 3;
    }
}
