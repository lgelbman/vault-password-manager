package com.example.vault;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import com.example.vault.data.Model;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;


public class HomeActivity extends AppCompatActivity {


    private Model model;
    private LinearLayout passwordsLayout;
    private Encrypter encrypter = new Encrypter();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        model = Model.getInstance(this);
        int hashedPin = encrypter.encryptPIN("1234");
        String s = encrypter.encryptPassword("1234", "abcdef");
        String s2 = encrypter.decryptPassword("1234", s);
        DisplayPasswords();
        setupFab();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, AddNewAccountActivity.class);
            startActivity(intent);
        });
    }
    

    private void DisplayPasswords() {
        passwordsLayout = getLinearLayout(R.id.passwords_layout);
        for (UserAccount account : model.getAccounts()) {
            setUpUserCredsLayout(account);
        }
    }

    private LinearLayout getLinearLayout(int p) {
        return findViewById(p);
    }

    private void setUpUserCredsLayout(UserAccount account) {
        LinearLayout userCredsLayout = (LinearLayout) LayoutInflater.from(HomeActivity.this)
                .inflate(R.layout.user_credentials_layout, null);
        passwordsLayout.addView(userCredsLayout);
        String userName = account.getUsername();
        String password = account.getPasswordHash();     //un-hash password here
        String accountType = account.getAccountType();
        String hiddenPassword = hidePassword(password);
        Button button = setUpButton(account, userCredsLayout, userName, password, hiddenPassword);
        TextView accountTypeTV = (TextView) userCredsLayout.getChildAt(0);
        setupCopyButton(userCredsLayout, userName, password, button);
        setUpAccountTypeTextView(accountType, accountTypeTV);
    }

    private void setUpAccountTypeTextView(String accountType, TextView accountTypeTV) {
        accountTypeTV.setText(accountType);
    }

    private void setupCopyButton(LinearLayout userCredsLayout, String userName, String password, Button button) {
        ImageButton copyButton = (ImageButton) userCredsLayout.getChildAt(2);
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(this.CLIPBOARD_SERVICE);
            String currentTextOnButton = button.getText().toString();
            ClipData clip;
            if (currentTextOnButton.equals(userName)) {
                clip = ClipData.newPlainText("password", userName);
            } else {
                clip = ClipData.newPlainText("password", password);
            }
            clipboard.setPrimaryClip(clip);
        });
    }

    @NotNull
    private Button setUpButton(UserAccount account, LinearLayout userCredsLayout, String userName, String password, String hiddenPassword) {
        Button button = (Button) userCredsLayout.getChildAt(1);
        button.setText(account.getUsername());
        button.setOnClickListener(v -> {
            String currentTextOnButton = button.getText().toString();
            if (currentTextOnButton.equals(userName)) {
                button.setText(hiddenPassword);
            } else if (currentTextOnButton.equals(hiddenPassword)) {
                button.setText(password);
            } else {
                button.setText(userName);
            }
        });
        return button;
    }

    private String hidePassword(String password){
        String hiddenPasswordSymbol = "•";
        String result = "";
        for (int i = 0; i < password.length(); i++){
            result = result + hiddenPasswordSymbol;
        }
        return result;
    }

}