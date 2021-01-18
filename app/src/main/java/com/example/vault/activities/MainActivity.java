package com.example.vault.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.vault.R;
import com.example.vault.data.Model;
import com.example.vault.data.UserAccount;
import com.example.vault.data.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends VaultAppActivity {

    private Model model;
    private LinearLayout passwordsLayout;
    private CoordinatorLayout cLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        cLayout = findViewById(R.id.home_activity);
        setSupportActionBar(toolbar);
        model = Model.getInstance(this);
        //int hashedPin = encrypter.encryptPIN("1234");
        //String s = encrypter.encryptPassword("1234", "abcdef");
        //String s2 = encrypter.decryptPassword("1234", s);
        setupFab();
        DisplayPasswords();
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
            Intent intent = new Intent(MainActivity.this, AddNewAccountActivity.class);
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
        LinearLayout userCredsLayout = (LinearLayout) LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.user_credentials_layout, null);
        passwordsLayout.addView(userCredsLayout);
        String username = account.getUsername();
        String password = account.getPasswordHash();     //un-hash password here
        String accountType = account.getAccountType();
        String hiddenPassword = hidePassword(password);
        Button button = setUpButton(account, userCredsLayout, username, password, hiddenPassword);
        TextView accountTypeTV = (TextView) userCredsLayout.getChildAt(0);
        setupCopyButton(userCredsLayout, username, password, button);
        setUpAccountTypeTextView(accountType, accountTypeTV);
    }

    private String hidePassword(String password) {
        String hiddenPasswordSymbol = "•";
        String result = "";
        for (int i = 0; i < password.length(); i++){
            result = result + hiddenPasswordSymbol;
        }
        return result;
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

    private void setupCopyButton(LinearLayout userCredsLayout, String username, String password, Button button) {
        ImageButton copyButton = (ImageButton) userCredsLayout.getChildAt(2);
        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager)
                    getSystemService(this.CLIPBOARD_SERVICE);
            String currentTextOnButton = button.getText().toString();
            ClipData clip;
            Snackbar snackbar;
            if (currentTextOnButton.equals(username)) {
                clip = ClipData.newPlainText("username", username);
                snackbar = Snackbar.make(cLayout,"username copied to clipboard", Snackbar.LENGTH_SHORT);
            } else {
                clip = ClipData.newPlainText("password", password);
                snackbar = Snackbar.make(cLayout,"password copied to clipboard", Snackbar.LENGTH_SHORT);
            }
            clipboard.setPrimaryClip(clip);
            snackbar.show();
        });
    }

    private void setUpAccountTypeTextView(String accountType, TextView accountTypeTV) {
        accountTypeTV.setText(accountType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.about) {
            Utils.showInfoDialog(MainActivity.this, R.string.about, R.string.about_info);
        }

        if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            logUserOut();
        }

        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}

