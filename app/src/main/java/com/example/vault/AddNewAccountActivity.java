package com.example.vault;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.vault.data.Model;


public class AddNewAccountActivity extends Activity {

    private Model model;
    private Bundle extras;
    private EditText usernameET;
    private EditText passwordET;
    private EditText accountTypeET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = Model.getInstance(this);
        extras = getIntent().getExtras();
        setUpLayout();
    }

    private void setUpLayout() {
        setContentView(R.layout.add_account_popup_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .5), (int) (height * .5));
        createViews();
    }

    private void createViews() {
        EditText usernameET = findViewById(R.id.create_username_et);
        EditText passwordET = findViewById(R.id.create_password_et);
        EditText accountTypeET = findViewById(R.id.account_type_et);
        Button createAccountButton = findViewById(R.id.create_account_button);
        if (extras != null) {
            usernameET.setText(extras.getString("username"));
            passwordET.setText(extras.getString("password"));
            accountTypeET.setText(extras.getString("type"));
            createAccountButton.setText("Change");
        }
        createAccountButton.setOnClickListener(v -> {
            String username = usernameET.getText().toString();
            String password = passwordET.getText().toString();
            String accountType = accountTypeET.getText().toString();
            //hash password here
            UserAccount userAccount = new UserAccount(username, password, accountType);
            model.addNewUser(userAccount, getApplicationContext());
            Intent restartHomeActivityIntent = new Intent(AddNewAccountActivity.this, HomeActivity.class);
            startActivity(restartHomeActivityIntent);
            finish();
        });
    }



}
