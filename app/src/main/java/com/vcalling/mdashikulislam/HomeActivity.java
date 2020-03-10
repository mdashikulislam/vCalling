package com.vcalling.mdashikulislam;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity {
    private Toolbar toolbar;
    BottomNavigationView navView;
    private FirebaseAuth auth;
    private ImageView imagePeople;
    private RecyclerView contactRecylerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        this.statusBarColorChange();
        this.toolbarSetting();
        this.initilization();
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


//        imagePeople.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this,FindPeopleActivity.class));
//            }
//        });
    }


    //Create Contact RecyleView
    public void recyleViewCreate(){
        contactRecylerView.setHasFixedSize(true);
        contactRecylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void initilization(){
        navView = findViewById(R.id.nav_view);
        contactRecylerView = findViewById(R.id.contactRecyleView);
        imagePeople = findViewById(R.id.imageView);

    }
    private void statusBarColorChange(){
        Utils.statusBarColor(this,R.color.orange);
    }
    private void toolbarSetting(){
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("V Calling");


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setDisplayShowTitleEnabled(true);
        //toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.navigation_home:
                            //Toast.makeText(HomeActivity.this,"Home Activity",Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(HomeActivity.this,HomeActivity.class);
                            startActivity(homeIntent);
                            break;
                        case R.id.navigation_setting:
                            Intent settingIntent = new Intent(HomeActivity.this,SettingsActivity.class);
                            startActivity(settingIntent);
                           // Toast.makeText(HomeActivity.this,"Setting Activity",Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.navigation_notifications:
                            Intent notiIntent = new Intent(HomeActivity.this,NotificationActivity.class);
                            startActivity(notiIntent);
//                            Toast.makeText(HomeActivity.this,"Notification Activity",Toast.LENGTH_SHORT).show();
                            break;
                        case  R.id.navigation_logout:
                            auth.signOut();
                            Intent logoutIntent = new Intent(HomeActivity.this, RegistrationActivity.class);
                            startActivity(logoutIntent);
                            finish();
                            break;
                    }

                    return true;
                }
            };


}
