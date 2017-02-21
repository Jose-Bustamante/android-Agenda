package com.example.jose_user.agendadeacoso;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Jose-User on 23/11/2015.
 */

public class Preferencias extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefs);
    }
}
