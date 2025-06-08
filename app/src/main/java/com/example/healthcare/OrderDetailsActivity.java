package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcare.DataBase;
import com.example.healthcare.Home_Activity;
import com.example.healthcare.R;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderDetailsActivity extends AppCompatActivity {

    private String[][] orderDetails = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    ListView lst;
    Button btn;
    TextView tvNoOrders;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.buttonODPBack);
        lst = findViewById(R.id.listViewODP);
        tvNoOrders = findViewById(R.id.textViewNoOrders);

        btn.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, Home_Activity.class)));

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("OrderDetailsActivity", "Username: " + username);

        db = new DataBase(getApplicationContext());
        ArrayList<String> dbData = null;
        try {
            dbData = db.getOrderData(username);
            Log.d("OrderDetailsActivity", "Raw dbData: " + dbData);
        } catch (Exception e) {
            Log.e("OrderDetailsActivity", "Error fetching order data: " + e.getMessage());
            lst.setVisibility(View.GONE);
            if (tvNoOrders != null) {
                tvNoOrders.setVisibility(View.VISIBLE);
                tvNoOrders.setText("Error loading orders: " + e.getMessage());
            }
            return;
        }

        if (dbData == null || dbData.isEmpty()) {
            lst.setVisibility(View.GONE);
            if (tvNoOrders != null) {
                tvNoOrders.setVisibility(View.VISIBLE);
                tvNoOrders.setText("No orders found");
            }
            return;
        }

        orderDetails = new String[dbData.size()][];
        for (int i = 0; i < orderDetails.length; i++) {
            orderDetails[i] = new String[5];
            try {
                String arrData = dbData.get(i).toString();
                String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                if (strData.length < 8) {
                    Log.w("OrderDetailsActivity", "Incomplete data at index " + i + ": " + arrData);
                    orderDetails[i][0] = "Unknown";
                    orderDetails[i][1] = "N/A";
                    orderDetails[i][2] = "N/A";
                    orderDetails[i][3] = "N/A";
                    orderDetails[i][4] = "N/A";
                    continue;
                }
                orderDetails[i][0] = strData[0]; // Fullname
                orderDetails[i][1] = strData[1]; // Address
                orderDetails[i][2] = "Rs." + strData[6]; // Amount
                orderDetails[i][3] = "Del: " + strData[4] + (strData[7].equals("medicine") ? "" : " " + strData[5]); // Date and optional time
                orderDetails[i][4] = strData[7]; // Type
            } catch (Exception e) {
                Log.e("OrderDetailsActivity", "Error parsing data at index " + i + ": " + e.getMessage());
                orderDetails[i][0] = "Error";
                orderDetails[i][1] = "Failed to parse data";
                orderDetails[i][2] = "N/A";
                orderDetails[i][3] = "N/A";
                orderDetails[i][4] = "N/A";
            }
        }

        list = new ArrayList<>();
        for (int i = 0; i < orderDetails.length; i++) {
            item = new HashMap<>();
            item.put("Line1", orderDetails[i][0]);
            item.put("Line2", orderDetails[i][1]);
            item.put("Line3", orderDetails[i][2]);
            item.put("Line4", orderDetails[i][3]);
            item.put("Line5", orderDetails[i][4]);
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"Line1", "Line2", "Line3", "Line4", "Line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        lst.setAdapter(sa);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}