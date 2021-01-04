package com.example.vault;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.view.View;

public class CreatePinActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }
}