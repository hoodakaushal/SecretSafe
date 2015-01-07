package com.sakhuja.ayush.secretsafe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class PassDialog extends Activity {

    int m = 0;
    ArrayList<String> p = new ArrayList<>();

    public void onOk(View view){
        m++;
        SharedPreferences sharedPreferences = this.getSharedPreferences("UserPassPreferences", this.MODE_PRIVATE);
        int n = sharedPreferences.getInt("n",0);
        EditText editText = (EditText) findViewById(R.id.password);
        String pass = editText.getText().toString();
        p.add(pass);
        editText.setText("");
        if (m == n){
            Intent intent = new Intent();
            intent.putExtra("p",p);
            setResult(1,intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_dialog);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pass_dialog, menu);
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
