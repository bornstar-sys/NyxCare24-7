package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class LabTest_Activity extends AppCompatActivity {
    private String[][] packages = {
            {"Package 1: Full Body Checkup", "", "", "", "999"},
            {"Package 2: Blood Glucose Fasting", "", "", "", "299"},
            {"Package 3: Covid-19 Antibody - IgG", "", "", "", "799"},
            {"Package 4: Thyroid Checkup", "", "", "", "399"},
            {"Package 5: Immunity Checkup", "", "", "", "899"}
    };
    private String[] package_detail = {
            "Blood Glucose Fasting\n" +
                    "HbA1c\n" +
                    "Iron Studies\n" +
                    "Kidney Function Test\n" +
                    "LDH Lactate Dehydrogenase, Serum\n" +
                    "Lipid Profile\n" +
                    "Liver Function Test",
            "Blood Glucose Fasting",
            "Covid-19 Antibody - IgG",
            "Thyroid Profile - Total (T3, T4 & TSH Ultra-sensitive)",
            "Complete Hemogram\n" +
                    "CRP (C Reactive Protein) Quantitative, Serum\n" +
                    "Iron Studies\n" +
                    "Kidney Function Test\n" +
                    "Vitamin D Total-25 Hydroxy\n" +
                    "Liver Function Test\n" +
                    "Lipid Profile"
    };

    HashMap<String, String> item;
    ArrayList list;
    SimpleAdapter sa;
    Button btnGoToCart, btnback;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lab_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGoToCart = findViewById(R.id.buttonLTCart);
        btnback = findViewById(R.id.buttonLTBack);
        listview = findViewById(R.id.listViewLabTest);

        btnback.setOnClickListener(view -> startActivity(new Intent(LabTest_Activity.this, Home_Activity.class)));

        list = new ArrayList();
        for (int i = 0; i < packages.length; i++) {
            item = new HashMap<>();
            item.put("Line1", packages[i][0]);
            item.put("Line2", packages[i][1]);
            item.put("Line3", packages[i][2]);
            item.put("Line4", packages[i][3]);
            item.put("Line5", "Total Cost: " + packages[i][4] + "/-");
            list.add(item);
        }
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"Line1", "Line2", "Line3", "Line4", "Line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        listview.setAdapter(sa);

        listview.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(LabTest_Activity.this, LabTest_Details_Activity.class);
            it.putExtra("text1", packages[i][0]);
            it.putExtra("text2", package_detail[i]);
            it.putExtra("text3", packages[i][4]);
            startActivity(it);
        });

        btnGoToCart.setOnClickListener(view -> startActivity(new Intent(LabTest_Activity.this, CartLabActivity.class)));
    }
}