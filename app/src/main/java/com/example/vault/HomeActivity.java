package com.example.vault;

import android.content.Intent;
import android.os.Bundle;

import com.example.vault.data.Model;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    List<UserAccount> accounts = new ArrayList<>();
    String fileName = "userAccountsFile.txt";
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //testMethod();
        model = Model.getInstance(this);
        DisplayPasswords();
        setupFab();
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddNewAccountActivity.class);
            startActivity(intent);

        });
    }

    private void testMethod() {
        for (int i = 0; i < 20; i++) {
            accounts.add(new UserAccount("goodUsername", "dgehsdfh"));
        }
    }


    private void DisplayPasswords() {
        LinearLayout passwordsLayout = findViewById(R.id.passwords_layout);
        for (UserAccount account : model.getAccounts()) {
            LinearLayout userCredsLayout = (LinearLayout)LayoutInflater.from(HomeActivity.this)
                    .inflate(R.layout.user_credentials_layout, null);
            passwordsLayout.addView(userCredsLayout);
        }
    }

    private void setHPLayoutParams(LinearLayout hpLayout) {
        hpLayout.setOrientation(LinearLayout.HORIZONTAL);
        hpLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        hpLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
    }

    private void addButtonsToLayout(LinearLayout hpLayout, UserAccount user) {
        Button credentialsButton = generateCredentialsButton(user);
        ImageButton copyButton = generateCopyButton();
        hpLayout.addView(credentialsButton);
        hpLayout.addView(copyButton);
    }

    private Button generateCredentialsButton(UserAccount user) {
        Button button = new Button(new ContextThemeWrapper(this, R.style.user_button), null, 0);
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        button.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setText(user.getUsername());
        return button;
    }

    private ImageButton generateCopyButton() {
        ImageButton button = new ImageButton(new ContextThemeWrapper(this, R.style.user_button), null, 0);
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        button.setImageResource(R.drawable.ic_baseline_content_copy_24);
        return button;
    }


}