package com.example.smartlunches.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.ProgressBar;

import com.example.smartlunches.R;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity){
        activity = myActivity;
    }

    public void enableloading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog_design, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    public void disableloading(){
        dialog.dismiss();
        dialog.cancel();
    }
}
