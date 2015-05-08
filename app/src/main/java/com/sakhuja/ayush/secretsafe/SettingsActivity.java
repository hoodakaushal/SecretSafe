package com.sakhuja.ayush.secretsafe;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Set;


public class SettingsActivity extends Activity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        System.out.println(key);
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener =
            new SharedPreferences.OnSharedPreferenceChangeListener() {
                public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                    System.out.println(key);
                    // listener implementation
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPassPreferences",this.MODE_PRIVATE);
        Set<String> users = sharedPreferences.getStringSet("ids", new HashSet<String>());
        String[] u = users.toArray(new String[users.size()]);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(listener);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment().newInstance(u))
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
