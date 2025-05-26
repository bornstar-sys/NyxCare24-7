package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LabTest_Details_Activity extends AppCompatActivity {

    TextView package_name, total_cost;
    EditText ed_details;
    Button btnAddToCart, btnback;
    DataBase db; // Declare at class level for onDestroy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab_test_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        package_name = findViewById(R.id.textViewLTDtittle);
        total_cost = findViewById(R.id.textViewCharge);
        ed_details = findViewById(R.id.editTextLTD);
        btnAddToCart = findViewById(R.id.buttonLTDCart);
        btnback = findViewById(R.id.buttonLTDBack);

        ed_details.setKeyListener(null);

        Intent intent = getIntent();
        package_name.setText(intent.getStringExtra("text1"));
        ed_details.setText(intent.getStringExtra("text2"));
        total_cost.setText("Total Cost: " + intent.getStringExtra("text3") + "/-");

        btnAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = package_name.getText().toString();
            float price = Float.parseFloat(intent.getStringExtra("text3"));

            Log.d("LabTest_Details_Activity", "Adding to cart: username=" + username + ", product=" + product + ", price=" + price);

            // Use the single-parameter constructor
            db = new DataBase(getApplicationContext());
            if (db.checkCart(username, product)) {
                Toast.makeText(getApplicationContext(), "Product already added", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.addCart(username, product, price, "lab");
                if (success) {
                    Toast.makeText(getApplicationContext(), "Record inserted to cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LabTest_Details_Activity.this, LabTest_Activity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnback.setOnClickListener(view -> startActivity(new Intent(LabTest_Details_Activity.this, LabTest_Activity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}