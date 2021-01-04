package com.example.vault;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreatePinActivity extends AppCompatActivity {

    final String VALID_PIN_PATTERN = "\\d\\d\\d\\d";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpLayout();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar
                .make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());
    }

    private void setUpLayout() {
        EditText enterPIN = findViewById(R.id.cp_enter_pin_et);
        EditText confirmPIN = findViewById(R.id.cp_confirm_pin_et);

        Button createButton = findViewById(R.id.cp_create_button);
        createButton.setOnClickListener(v -> {
            String pin1 = enterPIN.getText().toString();
            String pin2 = confirmPIN.getText().toString();

            if (isValidPIN(pin1, pin2)) {
                savePIN(pin1);
                logInUser();
            }
        });
    }

    private boolean isValidPIN(String pin1, String pin2) {
        TextView feedbackTV = findViewById(R.id.user_feedback_tv);

        if (!pin1.equals(pin2)) {
            // do something
            feedbackTV.setTextColor(Color.RED);
            feedbackTV.setText("PINs don't match");
            return false;
        }

        if (!pin1.matches(VALID_PIN_PATTERN)) {
            // do something
            feedbackTV.setTextColor(Color.RED);
            feedbackTV.setText("Invalid PIN pattern");
            return false;
        }
        return true;
    }

    private void savePIN(String pin) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.user_pin_key), pin);
        editor.apply();
    }

    private void logInUser() {
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsLoggedIn", true);
        editor.apply();
        finish();
    }
}