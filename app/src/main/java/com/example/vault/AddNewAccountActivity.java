package com.example.vault;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.vault.data.Model;


public class AddNewAccountActivity extends Activity {

    private Model model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = Model.getInstance(this);


        setContentView(R.layout.add_account_popup_window);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width*.5), (int) (height*.5));

        EditText userNameET = findViewById(R.id.create_username_et);
        EditText passwordET = findViewById(R.id.create_password_et);
        Button createAccountButton = findViewById(R.id.create_account_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();
                String password = passwordET.getText().toString();
                //hash password here
                UserAccount userAccount = new UserAccount(userName, password);
                model.addNewUser(userAccount, getApplicationContext());
                finish();
                Intent restartHomeActivityIntent = new Intent(AddNewAccountActivity.this, HomeActivity.class);
                startActivity(restartHomeActivityIntent);
            }
        });


    }

}
