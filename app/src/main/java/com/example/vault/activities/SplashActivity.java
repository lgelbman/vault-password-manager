package com.example.vault.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.vault.R;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;

public class SplashActivity extends AppCompatActivity
{
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate (savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        AppCompatDelegate.setDefaultNightMode
                (Build.VERSION.SDK_INT < 28 ? MODE_NIGHT_AUTO_BATTERY : MODE_NIGHT_FOLLOW_SYSTEM);
        checkLoginStatus();
        finish ();
    }
    private void checkLoginStatus() {
        if (!createdPIN()) {    // if no PIN created yet
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Intent createPinIntent = new Intent(this, CreatePinActivity.class);
            startActivity(createPinIntent);
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }

    private boolean createdPIN() {
        return sharedPreferences.contains(getString(R.string.user_pin_key));
    }

}
