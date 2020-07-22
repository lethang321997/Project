package com.example.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;

import com.example.project.R;
import com.example.project.adapter.TabLayoutAdapter;
import com.example.project.common.Constants;
import com.example.project.fragment.RootAddFragment;
import com.example.project.fragment.CartFragment;
import com.example.project.fragment.HomeFragment;
import com.example.project.fragment.ProfileFragment;
import com.example.project.model.User;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    public static User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        initData();
        initTabLayout();
    }

    void initData() {
        user = (User) getIntent().getSerializableExtra(Constants.USER);
    }

    void initWidget() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
    }

    void initTabLayout() {
        TabLayoutAdapter adapter = new TabLayoutAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Home");
        adapter.addFragment(new CartFragment(), "Cart");
        adapter.addFragment(new RootAddFragment(), "Stall");
        adapter.addFragment(new ProfileFragment(), "Profile");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_home_tab);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_add_tab);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_cart_tab);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_profile_tab);
        final int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tabSelected);
        final int tabUnselectIcon = ContextCompat.getColor(getApplicationContext(), R.color.tabUnselected);
        tabLayout.getTabAt(0).getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabUnselectIcon, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public User getUser() {
        return user;
    }

    public void logOut() {
        user = null;
        finish();
    }
}
