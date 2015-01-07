package com.sakhuja.ayush.secretsafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


public class Encrypt extends ActionBarActivity {

    ArrayList<String> p = new ArrayList<>();

    public class smail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPreferences sharedPreferences = Encrypt.this.getSharedPreferences("UserPassPreferences", Encrypt.this.MODE_PRIVATE);
            Set<String> ids = sharedPreferences.getStringSet("ids", new HashSet<String>());
            String[] id = ids.toArray(new String[ids.size()]);
            ArrayList<String> parts = split(args[1], ids.size());

            //System.out.println(p.size());

            int i = 0;
            while (i < ids.size()) {
                String host = Email.getHost(1,id[i]);
                final SendMail m = new SendMail(id[i], p.get(i),host);
                try {
                    if (m.send(id[i], p.get(i), id[i], id[i], args[0], parts.get(i))) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Encrypt.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Encrypt.this, "Email was not sent. Try again.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Encrypt.this, "Email was not sent. Try again.", Toast.LENGTH_LONG).show();
                        }
                    });
                }
                i++;
                if (i == ids.size()) {
                    p.clear();
                    finish();
                }
            }
            return null;
        }

        public ArrayList<String> split(String in, int n) {
            ArrayList<String> parts = new ArrayList<String>(Collections.nCopies(n, ""));
            for (int i = 0; i < in.length(); i++) {
                int sum = 0;
                for (int j = 0; j < n - 1; j++) {
                    Random rand = new Random();
                    int randomNum = rand.nextInt(1998) - 999;
                    parts.set(j, parts.get(j) + Integer.toString(randomNum) + " ");
                    sum += randomNum;
                }
                int m = in.charAt(i);
                parts.set(n - 1, parts.get(n - 1) + Integer.toString(m - sum) + " ");
            }
            return parts;
        }
//            ProgressBar progressBar = (ProgressBar) findViewById(R.id.sendprogress);
//            progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        //System.out.println("create");
        //System.out.println(p.size());
    }

    @Override
    public void onResume() {
        super.onResume();
        //System.out.println("restore");
        if (!p.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.cleartext);
            String cleartext = editText.getText().toString().trim();
            EditText editText1 = (EditText) findViewById(R.id.tag);
            String tag = editText1.getText().toString().trim();
            System.out.println(cleartext);
            System.out.println(tag);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.sendprogress);
            progressBar.setVisibility(View.VISIBLE);
            new smail().execute(tag, cleartext);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_encrypt, menu);
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

    public void send(View view) {
        //TODO Validate input
        Intent intent = new Intent(this, PassDialog.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            p = data.getStringArrayListExtra("p");
        }
    }
}