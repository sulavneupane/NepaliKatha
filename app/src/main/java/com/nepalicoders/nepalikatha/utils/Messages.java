package com.nepalicoders.nepalikatha.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Kahitashi on 1/26/2016.
 */
public class Messages {

    public static void snackBarLong(View v, String message){
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        //group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryLight));
        snackbar.show();
    }

    public static void snackBarShort(View v, String message){
        Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) snackbar.getView();
        //group.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.colorPrimaryLight));
        snackbar.show();
    }

    public static void alertDialogSimple(String title,String message, final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // set title
        alertDialogBuilder.setTitle(title);

        // set dialog message
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)

                /*
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity

                    }
                })*/

                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}