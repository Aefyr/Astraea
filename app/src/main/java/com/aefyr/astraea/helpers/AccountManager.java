package com.aefyr.astraea.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.aefyr.sombra.account.AccountData;

/**
 * Created by Aefyr on 09.09.2017.
 */

public class AccountManager {
    private static AccountManager instance;
    private SharedPreferences accountPrefs;
    private static final String ACCOUNT_PREFERENCES_NAME = "account";

    private AccountManager(Context c) {
        accountPrefs = c.getSharedPreferences(ACCOUNT_PREFERENCES_NAME, 0);
        instance = this;
    }

    public static AccountManager getInstance(Context c) {
        return instance == null ? new AccountManager(c) : instance;
    }

    public boolean isLoggedIn() {
        return accountPrefs.getBoolean(Key.IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        accountPrefs.edit().putBoolean(Key.IS_LOGGED_IN, loggedIn).apply();
    }

    public void setToken(String token) {
        accountPrefs.edit().putString(Key.TOKEN, token).apply();
    }

    public String getToken() {
        return accountPrefs.getString(Key.TOKEN, "0");
    }

    public void setAccountData(AccountData accountData) {
        SharedPreferences.Editor editor = accountPrefs.edit();

        editor.putString(Key.FIRST_NAME, accountData.getName(AccountData.FIRST_NAME));
        editor.putString(Key.MIDDLE_NAME, accountData.getName(AccountData.MIDDLE_NAME));
        editor.putString(Key.LAST_NAME, accountData.getName(AccountData.LAST_NAME));

        editor.putString(Key.EMAIL, accountData.email());

        editor.apply();
    }

    public String getEmail() {
        return accountPrefs.getString(Key.EMAIL, "astraea@example.com");
    }

    public String getNormalName() {
        return accountPrefs.getString(Key.LAST_NAME, "Barrera") + accountPrefs.getString(Key.FIRST_NAME, "Tony");
    }

    private class Key {
        private static final String IS_LOGGED_IN = "logged_in";
        private static final String TOKEN = "token";
        private static final String FIRST_NAME = "first_name";
        private static final String MIDDLE_NAME = "middle_name";
        private static final String LAST_NAME = "last_name";
        public static final String EMAIL = "email";
    }
}
