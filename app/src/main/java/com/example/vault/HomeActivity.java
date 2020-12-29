package com.example.vault;

import android.app.ActionBar;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Layout;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    List<UserAccount> accounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        testMethod();
        DisplayPasswords();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void testMethod() {
        for (int i = 0; i < 20; i++) {
            accounts.add(new UserAccount("goodUsername", "dgehsdfh"));
        }
    }


    private void DisplayPasswords() {
        LinearLayout pLayout = findViewById(R.id.passwords_layout);
        for (UserAccount user : accounts) {
            LinearLayout hpLayout = new LinearLayout(new ContextThemeWrapper
                    (this, R.style.user_account_layout), null, 0);
            setHPLayoutParams(hpLayout);
            addButtonsToLayout(hpLayout, user);
            pLayout.addView(hpLayout);
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
        Button button = new Button(new ContextThemeWrapper(this, R.style.standard_button), null, 0);
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        button.setGravity(Gravity.CENTER_HORIZONTAL);
        button.setText(user.getUsername());
        return button;
    }

    private ImageButton generateCopyButton() {
        ImageButton button = new ImageButton(this);
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        button.setImageResource(R.drawable.ic_baseline_content_copy_24);
        return button;
    }
}