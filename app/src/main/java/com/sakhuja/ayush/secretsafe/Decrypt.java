package com.sakhuja.ayush.secretsafe;

import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Decrypt extends ActionBarActivity implements SearchResultDialog.DialogListener{

    String path = "/storage/extSdCard/";

    List<List<Email>> emails = new ArrayList<>();
    ArrayList<String> p = new ArrayList<>();
    boolean flag=false;

    public class rmail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            long startTime = System.currentTimeMillis();
            SharedPreferences sharedPreferences = Decrypt.this.getSharedPreferences("UserPassPreferences", Decrypt.this.MODE_PRIVATE);
            Set<String> ids = sharedPreferences.getStringSet("ids", new HashSet<String>());
            String[] id = ids.toArray(new String[ids.size()]);
            System.out.println(ids.size());
            List<Email> email = new ArrayList<>();
            try{
            for (int i=0;i<ids.size();i++){

                    System.out.println(id[i]);
                    email = ReadMail.read(Email.getHost(2,id[i]),id[i],p.get(i),args[0],true);
                System.out.println(id[i]);
                System.out.println(email.size());
                    emails.add(email);
                long endTime = System.currentTimeMillis();
                System.out.println("Retrieval took " + (endTime - startTime) / 1000.0 + " seconds");
                    if (i==ids.size()-1){
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

    public class r_selected_mail extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... args) {
            SharedPreferences sharedPreferences = Decrypt.this.getSharedPreferences("UserPassPreferences", Decrypt.this.MODE_PRIVATE);
            Set<String> ids = sharedPreferences.getStringSet("ids", new HashSet<String>());
            String[] id = ids.toArray(new String[ids.size()]);
            //System.out.println(ids.size());
            List<Email> email = new ArrayList<>();
            try{
                for (int i=0;i<ids.size();i++){

                    System.out.println("for loop of r selected mail");
                    email = ReadMail.read(Email.getHost(2,id[i]),id[i],p.get(i),args[0],true);
                    System.out.println(Environment.getExternalStorageDirectory());
                    System.out.println(id[i]);
                    System.out.println(email.size());
                    System.out.println(email.get(0).attachments.get(0).name);
//                    System.out.println(email.get(0).attachments.get(0).path);
                    emails.add(email);
                }
            }

            catch (Exception e){
                e.printStackTrace();
            }
            flag=false;
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
    public void onSelection(String subject,int index) {
        //Handle decryption here

        System.out.println(emails.size());
        String text="";
        ArrayList<String> parts = new ArrayList<>();
        ArrayList<String> flags = new ArrayList<>();
        SharedPreferences sharedPreferences = Decrypt.this.getSharedPreferences("UserPassPreferences", Decrypt.this.MODE_PRIVATE);
        String body1 = emails.get(emails.size()-1).get(index).body;
        if(emails.get(emails.size()-1).get(index).attachments.size()>0){
            long startTime = System.currentTimeMillis();
//            flag=true;
//            emails.clear();
//            new r_selected_mail().execute(subject);
//            while(flag){
//
//            }
            ArrayList<String> files = new ArrayList<>();
            for (int i=0;i<emails.size();i++){
                if (emails.get(i).get(index).subject.equals(subject)){
                    files.add(path+emails.get(i).get(index).attachments.get(0).name);
                }
                else{
                    for (int j=0;i<emails.get(i).size();j++) {
                        if (emails.get(i).get(j).subject.equals(subject)) {
                            files.add(path+emails.get(i).get(j).attachments.get(0).name);
                            break;
                        }
                    }
                }
                System.out.println(files.get(files.size()-1));
                System.out.println(emails.get(i).get(index).attachments.get(0).size);
            }
            int k = sharedPreferences.getInt("k",0);
            try {
                Shamir.fileCombine(files,k);
            } catch (IOException e) {
                e.printStackTrace();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Decryption took " + (endTime - startTime) / 1000.0 + " seconds");
            /*for (int i=0;i<emails.size();i++){
                File file = new File(path+emails.get(i).get(0).attachments.get(0).name);
                file.delete();
            }*/
            //TODO make toast, stop spinner
//            flag=false;
        }
//        System.out.println(body1);
//        System.out.println(body1.substring(0,body1.indexOf(' ')));
//        System.out.println(body1.substring(0,body1.indexOf(' ')).trim());
        int space_index = body1.indexOf(' ');
        if (space_index==-1)space_index=0;
        if (body1.substring(0,space_index).trim().equals("Share")){
            System.out.println("entered shamir");
            for (int i = 0;i<emails.size();i++){
                if (emails.get(i).get(index).subject.equals(subject)){
                    parts.add(emails.get(i).get(index).body.split("=")[1].trim());
                    //System.out.println("part added =");
                    System.out.println(parts.get(parts.size()-1));
                    int ind= emails.get(i).get(index).body.indexOf(')');
                    flags.add("-s"+emails.get(i).get(index).body.substring(ind-1,ind));
                    //System.out.println("flag added =");
                    System.out.println(emails.get(i).get(index).body.substring(ind-1,ind));
                }
                else{
                    for (int j=0;i<emails.get(i).size();j++){
                        if (emails.get(i).get(j).subject.equals(subject)){
                            parts.add(emails.get(i).get(j).body.split("=")[1].trim());
                            System.out.println(parts.get(parts.size()-1));
                            int ind= emails.get(i).get(j).body.indexOf(')');
                            flags.add("-s"+emails.get(i).get(j).body.substring(ind-1,ind));
                            break;
                        }
                    }
                }
            }
            int k = sharedPreferences.getInt("k",0);
            //String text;
            text = SplitCombine.shamirCombine(parts,flags,k);
        }
        else{
            for (int i = 0;i<emails.size();i++){
                if (emails.get(i).get(index).subject.equals(subject)){
                    parts.add(emails.get(i).get(index).body);
                    System.out.println(parts.get(parts.size()-1));
                    //flags.add("-s"+Integer.toString(i+1));
                }
                else{
                    for (int j=0;i<emails.get(i).size();j++){
                        if (emails.get(i).get(j).subject.equals(subject)){
                            parts.add(emails.get(i).get(j).body);
                            //flags.add("-s"+Integer.toString(i+1));
                            break;
                        }
                    }
                }
            }
            //String text;
            text = SplitCombine.combine(parts);
        }
        /*for (int i = 0;i<emails.size();i++){
            if (emails.get(i).get(index).subject.equals(subject)){
                parts.add(emails.get(i).get(index).body);
                System.out.println(parts.get(parts.size()-1));
                flags.add("-s"+Integer.toString(i+1));
            }
            else{
                for (int j=0;i<emails.get(i).size();j++){
                    if (emails.get(i).get(j).subject.equals(subject)){
                        parts.add(emails.get(i).get(index).body);
                        flags.add("-s"+Integer.toString(i+1));
                        break;
                    }
                }
            }
        }*/
        /*int k = sharedPreferences.getInt("k",0);
        String text;
        if (k==0){
            text = SplitCombine.combine(parts);
        }
        else{
            text = SplitCombine.shamirCombine(parts,flags,k);
        }*/
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
