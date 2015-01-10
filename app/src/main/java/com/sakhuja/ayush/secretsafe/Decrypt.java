package com.sakhuja.ayush.secretsafe;

import android.app.DialogFragment;
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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Decrypt extends ActionBarActivity implements SearchResultDialog.DialogListener{

    List<List<Email>> emails = new ArrayList<>();
    ArrayList<String> p = new ArrayList<>();

    public class rmail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPreferences sharedPreferences = Decrypt.this.getSharedPreferences("UserPassPreferences", Decrypt.this.MODE_PRIVATE);
            Set<String> ids = sharedPreferences.getStringSet("ids", new HashSet<String>());
            String[] id = ids.toArray(new String[ids.size()]);
            System.out.println(ids.size());
            List<Email> email = new ArrayList<>();
            try{
            for (int i=ids.size()-1;i>=0;i--){

                    System.out.println(id[i]);
                    email = ReadMail.read(Email.getHost(2,id[i]),id[i],p.get(i),args[0]);
                    emails.add(email);
                    if (i==0){
                        ArrayList<String> subjects = new ArrayList<>();
                        for ( Email e : email ) {
                            subjects.add(e.subject);
                        }
                        DialogFragment sr = SearchResultDialog.newInstance(subjects);
                        sr.show(getFragmentManager(),"dialog");
                    }
                }
            }

            catch (Exception e){
                Log.e("App","Could not retrieve");
            }
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
    }

    //Handle dialog display
    @Override
    public void onResume() {
        super.onResume();
        if (!p.isEmpty()) {
            EditText editText = (EditText) findViewById(R.id.tag);
            String tag = editText.getText().toString();
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.retrieveprogress);
            progressBar.setVisibility(View.VISIBLE);
            new rmail().execute(tag);
        }
    }

    @Override
    public void onSelection(String subject,int index){
        //Handle decryption here
        //TODO Validate correctness
        System.out.println(emails.size());
        ArrayList<String> parts = new ArrayList<>();
        for (int i = 0;i<emails.size();i++){
            parts.add(emails.get(i).get(index).body);
            System.out.println(parts.get(parts.size()-1));
        }
        String text = SplitCombine.combine(parts);
        TextView textView = (TextView) findViewById(R.id.resultext);
        textView.setText(text);
        p.clear();
        emails.clear();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.retrieveprogress);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_decrypt, menu);
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

    public void retrieve (View view){
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
