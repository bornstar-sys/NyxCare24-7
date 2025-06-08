package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class Register_Activity extends AppCompatActivity {

    EditText edUsername, edEmail, edPassword, edConfirm;
    Button btn;
    TextView tvExistingUser;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edUsername = findViewById(R.id.editTextRigUsername);
        edEmail = findViewById(R.id.editTextRigEmail);
        edPassword = findViewById(R.id.editTextRigPassword);
        edConfirm = findViewById(R.id.editTextRigConfirmPassword2);
        tvExistingUser = findViewById(R.id.textViewuserExist);
        btn = findViewById(R.id.buttonRegister);
        db = new DataBase(this);

        btn.setOnClickListener(view -> {
            String username = edUsername.getText().toString().trim();
            String email = edEmail.getText().toString().trim();
            String password = edPassword.getText().toString().trim();
            String confirm = edConfirm.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                Toast.makeText(this, "Please fill all details!", Toast.LENGTH_SHORT).show();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(confirm)) {
                Toast.makeText(this, "Password and Confirm Password don't match!", Toast.LENGTH_SHORT).show();
            } else if (!isValid(password)) {
                Toast.makeText(this, "Password must be at least 8 characters, with a letter, digit, and special character!", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = db.register(username, email, password);
                if (success) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register_Activity.this, Login_Activity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Registration Failed! Username or Email may already exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvExistingUser.setOnClickListener(view -> startActivity(new Intent(Register_Activity.this, Login_Activity.class)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    private boolean isValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasLetter = false, hasDigit = false, hasSpecial = false;
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) { // Broader special character check
                hasSpecial = true;
            }
        }
        return hasLetter && hasDigit && hasSpecial;
    }
}