package com.example.vault;

import android.content.Intent;
import android.os.Bundle;

import com.example.vault.data.Model;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;


import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    List<UserAccount> accounts = new ArrayList<>();
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
            Button button = (Button) userCredsLayout.getChildAt(0);
            button.setText(account.getUsername());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userName = account.getUsername();
                    String password = account.getPasswordHash();     //un-hash password here
                    String hiddenPassword = hidePassword(password);
                    String currentTextOnButton = button.getText().toString();
                    if (currentTextOnButton.equals(userName)) {
                        button.setText(hiddenPassword);
                    } else if (currentTextOnButton.equals(hiddenPassword)) {
                        button.setText(password);
                    } else {
                        button.setText(userName);
                    }
                }
            });
            ImageButton copyButton = (ImageButton) userCredsLayout.getChildAt(1);
        }
    }

    private String hidePassword(String password){
        String hiddenPasswordSymbol = "•";
        String result = "";
        for (int i = 0; i < password.length(); i++){
            result = result + hiddenPasswordSymbol;
        }
        return result;
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