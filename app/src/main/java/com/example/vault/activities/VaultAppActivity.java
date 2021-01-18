package com.example.vault.activities;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public abstract class VaultAppActivity extends AppCompatActivity {

    protected SharedPreferences sharedPreferences;

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!stayLoggedIn()) {      // stay logged in setting is set to false
            logUserOut();
        }
    }

    protected boolean stayLoggedIn() {
        return sharedPreferences.getBoolean("stay", true);
    }

    protected void logUserOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IsLoggedIn", false);
        editor.apply();
        startActivity(new Intent(this, SplashActivity.class));
        finish();
    }

}
