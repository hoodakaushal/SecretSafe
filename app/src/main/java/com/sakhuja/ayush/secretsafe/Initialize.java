package com.sakhuja.ayush.secretsafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.HashSet;
import java.util.Set;


public class Initialize extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_initialize, menu);
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

    public void ok(View view){
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPassPreferences",this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        EditText editText = (EditText) findViewById(R.id.n_input);
        int n = Integer.parseInt(editText.getText().toString());
        //TODO validate n
        editor.putInt("n",n);
        editor.commit();
        sharedPreferences.getBoolean("shamir",false);
        RadioGroup secure = (RadioGroup) findViewById(R.id.secure);
        int selected = secure.getCheckedRadioButtonId();
        if (selected == -1){
            //TODO Make Toast
            //return;
        }
        else if (selected == R.id.robust_bt){
            editor.putBoolean("shamir",true);
            editor.commit();
        }
        RadioGroup inbuilt = (RadioGroup) findViewById(R.id.inbuilt);
        int select = inbuilt.getCheckedRadioButtonId();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.getBoolean("inbuilt",true);
        if (select == -1){
            //TODO Make toast
            //return;
        }
        else if (select == R.id.client_bt){
            SharedPreferences.Editor edit = preferences.edit();
            edit.putBoolean("inbuilt",false);
            edit.commit();
        }
        Intent intent;
        editor.remove("ids");
        editor.commit();
        Set<String> ids = sharedPreferences.getStringSet("ids",new HashSet<String>());
        Set<String> ids1 = ids;
//        ids1.add("cooldude_ayush9@yahoo.com");
        ids1.add("ayush.inferno@gmail.com");
        ids1.add("hoodakaushalreloaded@gmail.com");
        editor.putStringSet("ids",ids1);
        editor.commit();
        intent = new Intent(this,Initialize_k.class);
        startActivity(intent);
        finish();
    }
}
