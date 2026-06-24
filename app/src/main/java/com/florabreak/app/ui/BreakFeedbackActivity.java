package com.florabreak.app.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.florabreak.app.R;

public class BreakFeedbackActivity extends AppCompatActivity {

    private TextView backToBreakButton;
    private Button saveBreakButton;
    private Button backHomeButton;
    private TextView star1;
    private TextView star2;
    private TextView star3;
    private TextView star4;
    private TextView star5;
    private TextView ratingHintText;

    private int selectedRating = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_feedback);

        backToBreakButton = findViewById(R.id.backToBreakButton);
        saveBreakButton = findViewById(R.id.saveBreakButton);
        backHomeButton = findViewById(R.id.backHomeButton);
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        ratingHintText = findViewById(R.id.ratingHintText);

        star1.setOnClickListener(view -> setRating(1));
        star2.setOnClickListener(view -> setRating(2));
        star3.setOnClickListener(view -> setRating(3));
        star4.setOnClickListener(view -> setRating(4));
        star5.setOnClickListener(view -> setRating(5));

        backToBreakButton.setOnClickListener(view -> finish());

        saveBreakButton.setOnClickListener(view -> {String message;

            if (selectedRating > 0) {
                message = "Pause gespeichert: " + selectedRating + " Sterne";
            } else {
                message = "Pause gespeichert";
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(BreakFeedbackActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        backHomeButton.setOnClickListener(view -> {
            Intent intent = new Intent(BreakFeedbackActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
    private void setRating(int rating) {
        selectedRating = rating;

        TextView[] stars = {star1, star2, star3, star4, star5};

        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setText("★");
                stars[i].setTextColor(Color.parseColor("#E5A93D"));
            } else {
                stars[i].setText("☆");
                stars[i].setTextColor(Color.parseColor("#C7D1C9"));
            }
        }

        updateRatingHint(rating);
    }

    private void updateRatingHint(int rating) {
        switch (rating) {
            case 1:
                ratingHintText.setText("Eher nicht erholsam");
                break;
            case 2:
                ratingHintText.setText("War okay");
                break;
            case 3:
                ratingHintText.setText("Ganz gut");
                break;
            case 4:
                ratingHintText.setText("Hat gut getan");
                break;
            case 5:
                ratingHintText.setText("Richtig erholsam");
                break;
            default:
                ratingHintText.setText("Tippe auf die Sterne");
                break;
        }
    }
}