package com.example.distributedsystems2023;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.distributedsystems2023.databinding.ActivitySelectFilesBinding;
import com.example.distributedsystems2023.databinding.ActivityTotalStatsBinding;

public class SelectFilesActivity extends AppCompatActivity {

    private static final int REQUEST_PICK_FILE = 1;
    private ActivitySelectFilesBinding binding;
    private Button filePickerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_files);
        binding = ActivitySelectFilesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.assignBackButtonListener();
        filePickerButton = findViewById(R.id.filePickerButton);
        filePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFilePicker();
            }
        });

    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            Intent intent = new Intent(this, WalkStatsActivity.class);
            intent.putExtra("fileuri", uri.toString());
            startActivity(intent);
        }
    }

    private void assignBackButtonListener() {
        binding.BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}