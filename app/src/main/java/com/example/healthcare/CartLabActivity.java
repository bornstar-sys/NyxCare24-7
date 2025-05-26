package com.example.healthcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import java.util.Locale;

public class CartLabActivity extends AppCompatActivity {

    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    TextView tvtotal;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button datebutton, timebutton, btnCheckout, btnBack;
    private String[][] packages = {};
    ListView lst;
    DataBase db; // Declare at class level for onDestroy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart_lab);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        datebutton = findViewById(R.id.buttonCartDate);
        timebutton = findViewById(R.id.buttonCartTime);
        btnCheckout = findViewById(R.id.buttoncheckout);
        btnBack = findViewById(R.id.buttonCartback);
        tvtotal = findViewById(R.id.textViewCartCost);
        lst = findViewById(R.id.listViewCartmain);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        Log.d("CartLabActivity", "Username: " + username); // Log username for debugging

        // Initialize DataBase with single parameter
        db = new DataBase(getApplicationContext());
        ArrayList dbData = db.getCartData(username, "lab");
        Log.d("CartLabActivity", "Cart Data: " + dbData.toString()); // Log cart data

        if (dbData.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No items in cart", Toast.LENGTH_LONG).show();
            btnCheckout.setEnabled(false); // Disable checkout if cart is empty
        } else {
            Toast.makeText(getApplicationContext(), "Cart items: " + dbData.size(), Toast.LENGTH_LONG).show();
            btnCheckout.setEnabled(true); // Enable checkout if cart has items
        }

        packages = new String[dbData.size()][];
        for (int i = 0; i < packages.length; i++) {
            packages[i] = new String[5];
        }
        float totalamount = 0;
        for (int i = 0; i < dbData.size(); i++) {
            String arrData = dbData.get(i).toString();
            String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
            packages[i][0] = strData[0];
            packages[i][4] = "Cost: " + strData[1] + "/-";
            totalamount += Float.parseFloat(strData[1]);
        }

        tvtotal.setText("Total Cost: " + totalamount);
        list = new ArrayList<>();
        for (int i = 0; i < packages.length; i++) {
            item = new HashMap<>();
            item.put("line1", packages[i][0]);
            item.put("line2", packages[i][1]);
            item.put("line3", packages[i][2]);
            item.put("line4", packages[i][3]);
            item.put("line5", packages[i][4]);
            list.add(item);
        }
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});

        lst.setAdapter(sa);

        btnBack.setOnClickListener(view -> startActivity(new Intent(CartLabActivity.this, LabTest_Activity.class)));

        btnCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartLabActivity.this, Book_Appointment_Activity.class);
            it.putExtra("price", tvtotal.getText().toString());
            it.putExtra("date", datebutton.getText().toString());
            it.putExtra("time", timebutton.getText().toString());
            startActivity(it);
        });

        setDatePickerDialog();
        datebutton.setOnClickListener(view -> datePickerDialog.show());

        setTimePickerDialog();
        timebutton.setOnClickListener(view -> timePickerDialog.show());
    }

    private void setDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            datebutton.setText(day + "/" + month + "/" + year);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000);
    }

    private void setTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timebutton.setText(formattedTime);
        };

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hour, minute, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}