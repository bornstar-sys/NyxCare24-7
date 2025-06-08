package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home_Activity extends AppCompatActivity {

    private static final String PREF_WELCOME_SHOWN = "hasShownWelcome";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Check if welcome message has been shown
        boolean hasShownWelcome = sharedPreferences.getBoolean(PREF_WELCOME_SHOWN, false);
        if (!hasShownWelcome) {
            Toast.makeText(getApplicationContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();
            // Mark welcome message as shown
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PREF_WELCOME_SHOWN, true);
            editor.apply();
        }

        CardView exit = findViewById(R.id.cardExit);
        exit.setOnClickListener(view -> {
            // Clear all preferences, including welcome flag
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(Home_Activity.this, Login_Activity.class));
            finish(); // Close Home_Activity to prevent back navigation
        });

        CardView findDoctor = findViewById(R.id.cardFinddoctor);
        findDoctor.setOnClickListener(view ->
                startActivity(new Intent(Home_Activity.this, FindDoctor_Activity.class)));

        CardView labTest = findViewById(R.id.cardLabtest);
        labTest.setOnClickListener(view ->
                startActivity(new Intent(Home_Activity.this, LabTest_Activity.class)));

        CardView orderDetails = findViewById(R.id.cardOrderDetails);
        orderDetails.setOnClickListener(view ->
                startActivity(new Intent(Home_Activity.this, OrderDetailsActivity.class)));

        CardView buyMedicine = findViewById(R.id.cardBuymedicine);
        buyMedicine.setOnClickListener(view ->
                startActivity(new Intent(Home_Activity.this, BuyMedicineActivity.class)));

        CardView healthArticle = findViewById(R.id.cardHealthArticle);
        healthArticle.setOnClickListener(view ->
                startActivity(new Intent(Home_Activity.this, HeathArticleActivity.class)));
    }
}