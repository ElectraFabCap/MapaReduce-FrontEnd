package com.example.distributedsystems2023;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.distributedsystems2023.databinding.ActivityMainBinding;
import com.example.distributedsystems2023.utils.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity {

    private static final String user_id = "user_id";
    private ActivityMainBinding binding;

    private LinearLayout layoutFile;
    private LinearLayout layoutWalk;
    private LinearLayout layoutTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutFile = findViewById(R.id.layoutFile);
        layoutWalk = findViewById(R.id.layoutWalk);
        layoutTotal = findViewById(R.id.layoutTotal);
        this.assignLogoutButtonListener();

        layoutFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WalkStatsActivity.class);
                startActivity(intent);
            }
        });

        layoutWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserStatsActivity.class);
                startActivity(intent);
            }
        });

        layoutTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TotalStatsActivity.class);
                startActivity(intent);
            }
        });

    }


    private void assignLogoutButtonListener() {
        binding.LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
                goToLogin();
            }
        });
    }


    private void logout() {
        //might not use this at all :/
    }

    private void goToLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}