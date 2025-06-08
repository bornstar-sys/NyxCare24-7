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

public class BuyMedicineDetailsActivity extends AppCompatActivity {

    TextView medicineName, totalCost;
    EditText edDetails;
    Button btnAddToCart, btnBack;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_medicine_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        medicineName = findViewById(R.id.textViewBMDTitle);
        totalCost = findViewById(R.id.textViewBMDCost);
        edDetails = findViewById(R.id.editTextBMDDetails);
        btnAddToCart = findViewById(R.id.buttonBMDAddToCart);
        btnBack = findViewById(R.id.buttonBMDBack);

        edDetails.setKeyListener(null); // Make EditText read-only

        Intent intent = getIntent();
        medicineName.setText(intent.getStringExtra("text1"));
        edDetails.setText(intent.getStringExtra("text2"));
        totalCost.setText("Total Cost: " + intent.getStringExtra("text3") + "/-");

        btnAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = medicineName.getText().toString();
            float price = Float.parseFloat(intent.getStringExtra("text3"));

            Log.d("BuyMedicineDetailsActivity", "Adding to cart: username=" + username + ", product=" + product + ", price=" + price);

            db = new DataBase(getApplicationContext());
            if (db.checkCart(username, product)) {
                Toast.makeText(getApplicationContext(), "Medicine already in cart", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.addCart(username, product, price, "medicine");
                if (success) {
                    Toast.makeText(getApplicationContext(), "Medicine added to cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(view -> startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}