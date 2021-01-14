package com.example.vault;

import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.vault.data.Model;

import com.example.vault.data.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import android.preference.PreferenceManager;
import android.view.ContextThemeWrapper;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Switch;
import android.widget.TextView;


import org.jetbrains.annotations.NotNull;


public class HomeActivity extends AppCompatActivity {


    private Model model;
    private LinearLayout passwordsLayout;
    private Encrypter encrypter = new Encrypter();
    private SharedPreferences sharedPreferences;
    private CoordinatorLayout cLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        cLayout = findViewById(R.id.home_activity);
        setSupportActionBar(toolbar);
        model = Model.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
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
        setLongPressListener(userCredsLayout);
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
            Snackbar snackbar;
            if (currentTextOnButton.equals(userName)) {
                clip = ClipData.newPlainText("username", userName);
                snackbar = Snackbar.make(cLayout,"username copied to clipboard", Snackbar.LENGTH_SHORT);
            } else {
                clip = ClipData.newPlainText("password", password);
                snackbar = Snackbar.make(cLayout,"password copied to clipboard", Snackbar.LENGTH_SHORT);
            }
            clipboard.setPrimaryClip(clip);
            snackbar.show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.about) {
            Utils.showInfoDialog(HomeActivity.this, R.string.about, R.string.about_info);
        }

        if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            logout();
        }

        if (id == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //user preference is set to logout when the app is not in the foregound
        if (!stayloggedIn()) {
            logout();
        }
    }

    private boolean stayloggedIn() {
        return sharedPreferences.getBoolean("STAY_LOGGED_IN", false);
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsLoggedIn", false);
        editor.apply();
        finish();
    }

    private void addEditButton(UserAccount userAccount, LinearLayout passwordsLayout) {
        Button editBtn = new Button(this);
        editBtn.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        editBtn.setText("Edit");
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddNewAccountActivity.class);
                intent.putExtra("type", userAccount.getAccountType());
                intent.putExtra("username", userAccount.getUsername());
                intent.putExtra("password", userAccount.getPasswordHash());
                startActivity(intent);
            }
        });
        passwordsLayout.addView(editBtn);
    }

    private void setLongPressListener(View v) {
        v.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                double currentX = v.getX();
                float amountToMoveV;
                if (currentX == 0.0){
                    amountToMoveV = -250.0f;
                } else {
                    amountToMoveV = 0.0f;
                }
                ObjectAnimator animation = ObjectAnimator.ofFloat(v, "translationX", amountToMoveV);
                animation.setDuration(500);
                animation.start();

                return false;
            }
        });
    }


}