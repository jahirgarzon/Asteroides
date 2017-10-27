package org.example.asteroides;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.example.asteroides.R;

import java.util.prefs.Preferences;

/**
 * Created by jay on 10/18/17.
 */

public class PreferenciasFragment extends PreferenceFragment{
    @Override
    public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferencias);
        SharedPreferences pref = getPreferenceManager().getSharedPreferences();

        final EditTextPreference fragmentos = (EditTextPreference)findPreference(
                "fragmentos");
        fragmentos.setSummary( "En cuantos trozos se divide un asteroide ("+ pref.getString("fragmentos","?")+")");
        fragmentos.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object
                            newValue) {
                        int valor;
                        try {
                            valor = Integer.parseInt((String)newValue);
                        } catch(Exception e) {
                            Toast.makeText(getActivity(), "Ha de ser un número",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                        if (valor>=0 && valor<=9) {
                            fragmentos.setSummary(
                                    "En cuantos trozos se divide un asteroide ("+valor+")");
                            return true;
                        } else {
                            Toast.makeText(getActivity(), "Máximo de fragmentos 9",
                                    Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                });
    }

}
