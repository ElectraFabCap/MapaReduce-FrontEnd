package com.example.distributedsystems2023;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.distributedsystems2023.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final EditText usernameEditText = binding.username;
        final EditText ipEditText= binding.ip;
        final Button loginButton = binding.login;
        final ProgressBar loadingProgressBar = binding.loading;

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String ip = ipEditText.getText().toString().trim();

                if (checkUsername(username) && checkIP(ip)) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingProgressBar.setVisibility(View.GONE);
                            gotoMain(username,ip);
                        }
                    }, 2000);
                } else {

                    loginFail();
                }


            }
        });


    }


    private boolean checkUsername(String username) {
       return username.equals("1") || username.equals("2") || username.equals("3");

    }

    private boolean checkIP(String ip) {
        String ipPattern = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
               "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
               "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        return ip.matches(ipPattern);

    }


    private void loginFail() {
        Toast.makeText(LoginActivity.this, "Username or IP are invalid.", Toast.LENGTH_SHORT).show();
    }

    private void gotoMain(String username, String ip) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("ip", ip);
        startActivity(intent);
        finish();
    }
}