package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    TextView package_name,total_cost;
    EditText ed_details;
    Button btnAddToCart , btnback;


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
        total_cost.setText("Total Cost : "+intent.getStringExtra("text3")+"/-");

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username","").toString();
                String product = package_name.getText().toString();
                float price = Float.parseFloat(intent.getStringExtra("text3").toString());

                DataBase db = new DataBase(getApplicationContext(),"healthcare",null,1);
                if (db.checkCart(username,product)==1){
                    Toast.makeText(getApplicationContext(),"Product already Added",Toast.LENGTH_SHORT).show();
                }else{
                    db.addCart(username,product,price,"lab");
                    Toast.makeText(getApplicationContext(),"Record Inserted to Cart",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LabTest_Details_Activity.this,LabTest_Activity.class));
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LabTest_Details_Activity.this,LabTest_Activity.class));
            }
        });


    }
}