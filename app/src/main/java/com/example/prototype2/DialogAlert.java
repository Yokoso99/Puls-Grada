package com.example.prototype2;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Firebase;

public class DialogAlert extends DialogFragment {

    private RatingBar ratingBar;
    private EditText editText;
    private TextView tv;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Inflate the custom layout
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.review_layout, null);

        // Find the RatingBar and EditText in the layout
        ratingBar = dialogLayout.findViewById(R.id.ratingbar);
        editText = dialogLayout.findViewById(R.id.edittext);
        tv = dialogLayout.findViewById(R.id.tv);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(dialogLayout);


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating==0){
                    tv.setText("Very Disatisfied");
                } else if (rating==1) {
                    tv.setText("Disatisfied");
                } else if (rating==2 || rating==3) {
                    tv.setText("OK");
                } else if (rating==4) {
                    tv.setText("Satisfied");
                } else if (rating==5) {
                    tv.setText("Very Satisfied");
                }
            }
        });
        float rating = ratingBar.getRating();
        String feedback = editText.getText().toString();







        return builder.create();
    }
}
