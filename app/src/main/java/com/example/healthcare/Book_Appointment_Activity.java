package com.example.healthcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class Book_Appointment_Activity extends AppCompatActivity {

    EditText ed1, ed2, ed3, ed4;
    TextView tv;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Button dateButton, timeButton, btnBook, btnback;
    private DataBase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment);

        // Initialize DataBase with only Context
        dbHelper = new DataBase(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        tv = findViewById(R.id.textViewBookAppointmenttext);
        ed1 = findViewById(R.id.editTextBAFullName);
        ed2 = findViewById(R.id.editTextBAAddress);
        ed3 = findViewById(R.id.editTextBAContactNumber);
        ed4 = findViewById(R.id.editTextBAFees);
        dateButton = findViewById(R.id.buttonBADate);
        timeButton = findViewById(R.id.buttonBATime);
        btnBook = findViewById(R.id.buttonBAdone);
        btnback = findViewById(R.id.buttonBack);

        // Get title and fee from Intent
        Intent it = getIntent();
        //String title = it.getStringExtra("text1"); // Commented out as in your code
        String fee = it.getStringExtra("text5");

        // Set title (remains unchanged)
        //tv.setText(title);

        // Set fee (fetched from Intent)
        ed4.setText("Cons fee: " + fee + "/-");
        ed4.setKeyListener(null);  // Make fee field read-only

        // Initialize pickers
        setDatePickerDialog();
        setTimePickerDialog();

        // Set click listeners
        dateButton.setOnClickListener(view -> datePickerDialog.show());
        timeButton.setOnClickListener(view -> timePickerDialog.show());

        btnback.setOnClickListener(view ->
                startActivity(new Intent(Book_Appointment_Activity.this, FindDoctor_Activity.class)));

        btnBook.setOnClickListener(view -> {
            // Get all input values
            String fullName = ed1.getText().toString().trim();
            String address = ed2.getText().toString().trim();
            String phone = ed3.getText().toString().trim();
            String appointmentFee = fee;  // Use fee from Intent
            String date = dateButton.getText().toString();
            String time = timeButton.getText().toString();

            // Validate inputs
            if (fullName.isEmpty() || address.isEmpty() || phone.isEmpty() ||
                    date.equals("Select Date") || time.equals("Select Time")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Store in database
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("title", title); // Commented out as in your code
            values.put("full_name", fullName);
            values.put("address", address);
            values.put("phone", phone);
            values.put("fee", appointmentFee);
            values.put("date", date);
            values.put("time", time);

            long result = db.insert("appointments", null, values);

            if (result != -1) {
                Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                // Clear fields after successful booking
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");
                dateButton.setText("Select Date");
                timeButton.setText("Select Time");
            } else {
                Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
            }

            db.close();
        });
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

    private void setTimePickerDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            String formattedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            timeButton.setText(formattedTime);
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
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}