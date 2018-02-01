package com.tseluikoartem.ening.yandexmobdevproject.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;


import com.tseluikoartem.ening.yandexmobdevproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import launcher.IconRecycleAdapter;
import launcher.OffsetItemDecoration;

public class ScrollingLauncActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View navigationHeaderView = navigationView.getHeaderView(0);
        final View profileImage = navigationHeaderView.findViewById(R.id.imageViewHeader);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(getApplicationContext(),GreetingActivity.class);
                startActivity(intent);
            }
        });

        createGridLayout();
    }


    private void createGridLayout() {
        final RecyclerView recyclerView = findViewById(R.id.louncher_content);
        recyclerView.setHasFixedSize(true);
        final int offset = 8;
        recyclerView.addItemDecoration(new OffsetItemDecoration(offset));

        final int spanCount = 4;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        recyclerView.setLayoutManager(layoutManager);

        final List<Integer> data = generateData();
        final IconRecycleAdapter launcherAdapter = new IconRecycleAdapter(data);
        recyclerView.setAdapter(launcherAdapter);
    }


    private List<Integer> generateData() {
        final List<Integer> colors = new ArrayList<>();
        final Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            colors.add(color);
        }

        return colors;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        Intent intent=null;

        if (id == R.id.nav_launcher) {
            intent=new Intent(this,ScrollingLauncActivity.class);
        } else if (id == R.id.nav_list) {
            intent=new Intent(this,ScrollingListActivity.class);
        } else if (id == R.id.nav_settings) {
            intent=new Intent(this,GreetingActivity.class);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_launcher_activity);
        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }
}
