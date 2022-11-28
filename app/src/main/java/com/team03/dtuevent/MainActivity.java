package com.team03.dtuevent;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.team03.dtuevent.databinding.ActivityMainBinding;
//import com.team03.dtuevent.ui.CSOnboarding;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE_INTRO = 1000;
    private static final int REQUEST_CODE_CAMERA = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.team03.dtuevent.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host);
        if (navHostFragment == null) {
            throw new RuntimeException("No NavHost found");
        }
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomNav, navController);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);

        }


        binding.bottomNav.setOnNavigationItemReselectedListener(item -> {
        });

//        Settings s = Settings.getInstance(this);
//        if (s.getShouldShowOnboarding()) {
//            Intent i = new Intent(this, CSOnboarding.class);
//            startActivityForResult(i, REQUEST_CODE_INTRO
//            );
//        }

        if (savedInstanceState == null) {
            handleIntent(getIntent(), navController); // First launch
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INTRO) {
            if (resultCode == Activity.RESULT_OK) {
                recreate(); // Previous changes no longer applicable
            } else {
                finish();
            }
        }
    }

    private void handleIntent(Intent intent, NavController navController)  {
        if (intent == null || intent.getType() == null) return;
        if (intent.getType().startsWith("image")) {
            navigate(navController, R.id.scannerFragment);
        } else {
            navigate(navController, R.id.searchFragment);

        }
    }


    private void navigate(NavController navController, int id) {

        navController.navigate(id);
    }


}