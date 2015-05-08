package com.sakhuja.ayush.secretsafe;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
/**
 * Created by Ayush on 1/18/2015.
 */
public class SettingsFragment extends PreferenceFragment {
    static SettingsFragment newInstance(String[] users){
        SettingsFragment s = new SettingsFragment();
        Bundle args = new Bundle();
        args.putStringArray("users", users);
        s.setArguments(args);
        return s;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        final String[] users = getArguments().getStringArray("users");
        for (int i=0;i<users.length;i++){
            CheckBoxPreference checkbox = new CheckBoxPreference(getActivity());
            checkbox.setTitle(users[i]);
            checkbox.setKey(users[i]);
            checkbox.setChecked(true);
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceScreen.addPreference(checkbox);
        }
    }
}
