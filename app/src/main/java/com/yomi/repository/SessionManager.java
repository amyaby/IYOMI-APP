package com.yomi.repository;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF_NAME = "yomi_session";
    private static final String KEY_PLAYER_ID = "player_id";
    private static final String KEY_PLAYER_NAME = "player_name";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createSession(long id, String name) {
        editor.putLong(KEY_PLAYER_ID, id);
        editor.putString(KEY_PLAYER_NAME, name);
        editor.apply();
    }

    public long getPlayerId() {
        return pref.getLong(KEY_PLAYER_ID, -1);
    }

    public String getPlayerName() {
        return pref.getString(KEY_PLAYER_NAME, null);
    }

    public boolean isLoggedIn() {
        return getPlayerId() != -1;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }
}
