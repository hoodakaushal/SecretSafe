package com.sakhuja.ayush.secretsafe;

/**
 * Created by Ayush on 1/1/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;

public class SearchResultDialog extends DialogFragment{
    public interface DialogListener {
        public void onSelection(String subject,int index);
    }
    DialogListener mListener;
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }
    static SearchResultDialog newInstance(ArrayList<String> subject){
        SearchResultDialog f = new SearchResultDialog();
        Bundle args = new Bundle();
        args.putStringArrayList("subject", subject);
        f.setArguments(args);
        return f;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        final ArrayList<String> subject = getArguments().getStringArrayList("subject");
        final CharSequence[] sub = subject.toArray(new CharSequence[subject.size()]);
        DialogInterface.OnClickListener im = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),sub[which],Toast.LENGTH_SHORT).show();
                //Activity callingActivity = (Activity) getActivity();
                mListener = (DialogListener) getActivity();
                mListener.onSelection(subject.get(which),which);
                dialog.dismiss();
                //TextView textView = (TextView) findViewById(R.id.textView1);
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose");
        builder.setItems(sub,im);
        return builder.create();
    }
}
