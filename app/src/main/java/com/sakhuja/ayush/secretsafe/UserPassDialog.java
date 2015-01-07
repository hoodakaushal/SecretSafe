package com.sakhuja.ayush.secretsafe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;


public class UserPassDialog extends Activity {

    int m = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass_dialog);
    }

    public void onOk(View view){
        m++;
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPassPreferences", this.MODE_PRIVATE);
        int n = sharedPreferences.getInt("n",0);
        Set<String> ids = sharedPreferences.getStringSet("ids",new HashSet<String>());
        EditText editText = (EditText) findViewById(R.id.username);
        String id = editText.getText().toString().toLowerCase();
        Set<String> ids1 = ids;
        ids1.add(id);
        //System.out.println(ids.size());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("ids",ids1);
        editor.commit();
        editText.setText("");
        EditText passtext = (EditText) findViewById(R.id.u_password);
        passtext.setText("");
        editText.requestFocus();
        if (m == n){
            //TODO Test IDS
            //System.out.println(ids.size());
            Intent intent;
            boolean shamir = sharedPreferences.getBoolean("shamir",false);
            if (shamir == false){
                intent = new Intent(this,MainMenu.class);
                startActivity(intent);
                finish();
            }
            else if (shamir == true){
                intent = new Intent(this,Initialize_k.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_pass_dialog, menu);
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
