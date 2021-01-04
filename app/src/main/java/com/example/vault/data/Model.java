package com.example.vault.data;

import android.content.Context;

import com.example.vault.UserAccount;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Model {

    private List<UserAccount> accounts;
    private String fileName = "userAccountsFile.txt";
    private static Model instance;

    public static synchronized Model getInstance(Context context) {
        if (instance == null){
            return new Model(context);
        }
        return instance;
    }

    private Model(Context context) {
        if (fileExists(context)){
            read(context);
        } else {
            createFile(context);
        }

    }

    public List<UserAccount> getAccounts() {
        return this.accounts;
    }

    public void addNewUser(UserAccount user, Context context){
        accounts.add(user);
        write(context);
    }

    private void write(Context context) {
        try {
            Gson gson = new Gson();
            String userAccountsJSON = gson.toJson(accounts);
            FileOutputStream fileOutputStream = context
                    .openFileOutput(fileName, MODE_PRIVATE);
            fileOutputStream.write(userAccountsJSON.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(Context context) {
        try {
            Gson gson = new Gson();
            String filePath = getFilePath(context);
            Type typeOfListOfUserAccounts = new TypeToken<List<UserAccount>>() {}.getType();
            JsonReader json_Reader = new JsonReader(new FileReader(filePath));
            accounts = gson.fromJson(json_Reader, typeOfListOfUserAccounts);
            json_Reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFilePath(Context context){
        return context
                .getFilesDir()
                .getAbsolutePath() + "/" + fileName;
    }

    private boolean fileExists(Context context){
        File file = new File(getFilePath(context));
        if (file.exists()){
            return true;
        }
        return false;
    }

    private void createFile(Context context) {
        File file = new File(getFilePath(context));
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
