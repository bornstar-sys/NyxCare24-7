package com.example.healthcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button dateButton, timeButton, btnBook, btnBack;
    private DataBase dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book_appointment);

        dbHelper = new DataBase(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tv = findViewById(R.id.textViewBookAppointmenttext);
        ed1 = findViewById(R.id.editTextBAFullName);
        ed2 = findViewById(R.id.editTextBAAddress);
        ed3 = findViewById(R.id.editTextBAContactNumber);
        ed4 = findViewById(R.id.editTextBAFees);
        dateButton = findViewById(R.id.buttonBADate);
        timeButton = findViewById(R.id.buttonBATime);
        btnBook = findViewById(R.id.buttonBAdone);
        btnBack = findViewById(R.id.buttonBack);

        Intent it = getIntent();
        String doctorName = it.getStringExtra("text1");
        String fee = it.getStringExtra("text5");

        tv.setText(doctorName);
        ed4.setText("Cons fee: " + fee + "/-");
        ed4.setKeyListener(null);

        setDatePickerDialog();
        setTimePickerDialog();

        dateButton.setOnClickListener(view -> datePickerDialog.show());
        timeButton.setOnClickListener(view -> timePickerDialog.show());

        btnBack.setOnClickListener(view ->
                startActivity(new Intent(Book_Appointment_Activity.this, FindDoctor_Activity.class)));

        btnBook.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            String patientName = ed1.getText().toString().trim(); // Optional patient name
            String address = ed2.getText().toString().trim();
            String phone = ed3.getText().toString().trim();
            String date = dateButton.getText().toString();
            String time = timeButton.getText().toString();

            if (patientName.isEmpty() || address.isEmpty() || phone.isEmpty() ||
                    date.equals("Select Date") || time.equals("Select Time")) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check for duplicate appointment
            if (dbHelper.checkAppointmentExists(username, doctorName, address, phone, date, time)) {
                Toast.makeText(this, "Appointment is already booked", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                float appointmentFee = Float.parseFloat(fee);
                dbHelper.addOrder(username, doctorName, address, phone, 0, date, time, appointmentFee, "appointment");

                Toast.makeText(this, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
                ed1.setText("");
                ed2.setText("");
                ed3.setText("");
                dateButton.setText("Select Date");
                timeButton.setText("Select Time");

                Intent intent = new Intent(Book_Appointment_Activity.this, OrderDetailsActivity.class);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid fee format", Toast.LENGTH_SHORT).show();
            }
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