package com.example.healthcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartMedicineActivity extends AppCompatActivity {

    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvTotal;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnCheckout, btnBack;
    private String[][] medicines = {};
    ListView lst;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_medicine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dateButton = findViewById(R.id.buttonBMCartDate);
        btnCheckout = findViewById(R.id.buttonBMcheckout);
        btnBack = findViewById(R.id.buttonBMCartback);
        tvTotal = findViewById(R.id.textViewCartCostBM);
        lst = findViewById(R.id.listViewCartBMmain);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("CartMedicineActivity", "Username: " + username);

        db = new DataBase(getApplicationContext());
        ArrayList dbData = db.getCartData(username, "medicine");
        Log.d("CartMedicineActivity", "Cart Data: " + dbData.toString());

        if (dbData.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No medicines in cart", Toast.LENGTH_LONG).show();
            btnCheckout.setEnabled(false);
        } else {
            Toast.makeText(getApplicationContext(), "Cart items: " + dbData.size(), Toast.LENGTH_LONG).show();
            btnCheckout.setEnabled(true);
        }

        medicines = new String[dbData.size()][];
        for (int i = 0; i < medicines.length; i++) {
            medicines[i] = new String[5];
        }
        float totalAmount = 0;
        for (int i = 0; i < dbData.size(); i++) {
            String arrData = dbData.get(i).toString();
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            medicines[i][0] = strData[0];
            medicines[i][4] = "Cost: " + strData[1] + "/-";
            totalAmount += Float.parseFloat(strData[1]);
        }

        tvTotal.setText("Total Cost: " + totalAmount);

        list = new ArrayList<>();
        for (int i = 0; i < medicines.length; i++) {
            item = new HashMap<>();
            item.put("line1", medicines[i][0]);
            item.put("line2", medicines[i][1]);
            item.put("line3", medicines[i][2]);
            item.put("line4", medicines[i][3]);
            item.put("line5", medicines[i][4]);
            list.add(item);
        }
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});

        lst.setAdapter(sa);

        btnBack.setOnClickListener(view -> startActivity(new Intent(CartMedicineActivity.this, BuyMedicineActivity.class)));

        btnCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartMedicineActivity.this, BuyMedicineOrderActivity.class);
            it.putExtra("price", tvTotal.getText().toString());
            it.putExtra("date", dateButton.getText().toString());
            it.putExtra("otype", "medicine");
            startActivity(it);
        });

        setDatePickerDialog();
        dateButton.setOnClickListener(view -> datePickerDialog.show());
    }

    private void setDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            dateButton.setText(day + "/" + month + "/" + year);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}