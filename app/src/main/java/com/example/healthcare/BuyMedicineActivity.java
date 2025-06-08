package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class BuyMedicineActivity extends AppCompatActivity {
    private String[][] medicines = {
            {"Paracetamol 500mg", "", "", "", "50"},
            {"Aspirin 100mg", "", "", "", "30"},
            {"Ibuprofen 200mg", "", "", "", "80"},
            {"Cetirizine 10mg", "", "", "", "60"},
            {"Amoxicillin 500mg", "", "", "", "150"},
            {"Azithromycin 250mg", "", "", "", "120"},
            {"Metformin 500mg", "", "", "", "90"},
            {"Amlodipine 5mg", "", "", "", "70"},
            {"Losartan 50mg", "", "", "", "100"},
            {"Omeprazole 20mg", "", "", "", "85"},
            {"Atorvastatin 10mg", "", "", "", "110"},
            {"Cefixime 200mg", "", "", "", "130"},
            {"Loratadine 10mg", "", "", "", "65"},
            {"Salbutamol Inhaler", "", "", "", "200"},
            {"Pantoprazole 40mg", "", "", "", "95"},
            {"Levothyroxine 50mcg", "", "", "", "75"},
            {"Montelukast 10mg", "", "", "", "90"},
            {"Prednisolone 5mg", "", "", "", "55"},
            {"Clopidogrel 75mg", "", "", "", "120"},
            {"Metoprolol 25mg", "", "", "", "80"},
            {"Doxycycline 100mg", "", "", "", "100"},
            {"Hydrochlorothiazide 25mg", "", "", "", "60"},
            {"Fexofenadine 120mg", "", "", "", "70"},
            {"Gabapentin 300mg", "", "", "", "140"},
            {"Tramadol 50mg", "", "", "", "110"},
            {"Sertraline 50mg", "", "", "", "130"},
            {"Fluoxetine 20mg", "", "", "", "125"},
            {"Citalopram 20mg", "", "", "", "120"},
            {"Ondansetron 4mg", "", "", "", "90"},
            {"Ranitidine 150mg", "", "", "", "65"}
    };

    private String[] medicine_details = {
            "Pain reliever and fever reducer\nUsed for headaches, muscle aches, and fever\nDosage: 1-2 tablets every 4-6 hours, max 4g daily",
            "Anti-inflammatory and pain reliever\nUsed for minor aches and pains, heart attack prevention\nDosage: 1 tablet daily, consult doctor for long-term use",
            "Nonsteroidal anti-inflammatory drug\nUsed for pain, inflammation, and fever\nDosage: 1-2 tablets every 6-8 hours, max 3200mg daily",
            "Antihistamine for allergy relief\nUsed for sneezing, runny nose, and itching\nDosage: 1 tablet daily, avoid alcohol",
            "Antibiotic for bacterial infections\nUsed for respiratory and urinary tract infections\nDosage: 1 capsule 3 times daily, complete course",
            "Antibiotic for bacterial infections\nUsed for pneumonia, sinusitis, and skin infections\nDosage: 1 tablet twice daily for 3-5 days",
            "Oral antidiabetic for type 2 diabetes\nControls blood sugar levels\nDosage: 1 tablet 1-2 times daily with meals",
            "Calcium channel blocker for hypertension\nTreats high blood pressure and chest pain\nDosage: 1 tablet daily",
            "Angiotensin receptor blocker for hypertension\nTreats high blood pressure and heart failure\nDosage: 1 tablet daily",
            "Proton pump inhibitor for acid reflux\nTreats GERD and ulcers\nDosage: 1 capsule daily before meals",
            "Statin for cholesterol management\nLowers LDL cholesterol and heart disease risk\nDosage: 1 tablet daily, preferably at night",
            "Antibiotic for bacterial infections\nUsed for urinary and respiratory infections\nDosage: 1 tablet twice daily for 7 days",
            "Antihistamine for allergies\nRelieves hay fever and urticaria symptoms\nDosage: 1 tablet daily",
            "Bronchodilator for asthma\nRelieves wheezing and shortness of breath\nDosage: 1-2 puffs as needed, max 4 times daily",
            "Proton pump inhibitor for acid-related disorders\nTreats GERD and peptic ulcers\nDosage: 1 tablet daily before meals",
            "Thyroid hormone replacement\nTreats hypothyroidism\nDosage: 1 tablet daily on an empty stomach",
            "Leukotriene inhibitor for asthma and allergies\nPrevents asthma attacks and allergic rhinitis\nDosage: 1 tablet daily in the evening",
            "Corticosteroid for inflammation\nTreats allergies, arthritis, and autoimmune conditions\nDosage: 1-4 tablets daily as prescribed",
            "Antiplatelet for heart disease prevention\nReduces risk of heart attack and stroke\nDosage: 1 tablet daily",
            "Beta-blocker for hypertension and heart conditions\nTreats high blood pressure and angina\nDosage: 1 tablet 1-2 times daily",
            "Antibiotic for bacterial infections\nUsed for acne, Lyme disease, and respiratory infections\nDosage: 1 capsule twice daily",
            "Diuretic for hypertension and edema\nTreats high blood pressure and fluid retention\nDosage: 1 tablet daily",
            "Antihistamine for allergy relief\nTreats hay fever and chronic urticaria\nDosage: 1 tablet daily",
            "Anticonvulsant for nerve pain\nTreats neuropathic pain and seizures\nDosage: 1 capsule 1-3 times daily",
            "Opioid analgesic for moderate pain\nTreats acute and chronic pain\nDosage: 1 tablet every 4-6 hours as needed",
            "SSRI for depression and anxiety\nTreats major depressive disorder and OCD\nDosage: 1 tablet daily, may increase as prescribed",
            "SSRI for depression and anxiety\nTreats depression, panic disorder, and PTSD\nDosage: 1 capsule daily in the morning",
            "SSRI for depression and anxiety\nTreats depression and generalized anxiety disorder\nDosage: 1 tablet daily",
            "Antiemetic for nausea and vomiting\nUsed for chemotherapy and post-surgery nausea\nDosage: 1 tablet every 8 hours as needed",
            "H2 receptor blocker for acid reflux\nTreats heartburn and peptic ulcers\nDosage: 1 tablet twice daily"
    };

    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    Button btnGoToCart, btnBack;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_medicine);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnGoToCart = findViewById(R.id.buttonPSCart);
        btnBack = findViewById(R.id.buttonPSBack);
        listView = findViewById(R.id.listViewBuymedicine);

        btnBack.setOnClickListener(view -> startActivity(new Intent(BuyMedicineActivity.this, Home_Activity.class)));

        btnGoToCart.setOnClickListener(view -> {
            Intent it = new Intent(BuyMedicineActivity.this, CartMedicineActivity.class);
            it.putExtra("otype", "medicine");
            startActivity(it);
        });

        list = new ArrayList<>();
        for (int i = 0; i < medicines.length; i++) {
            item = new HashMap<>();
            item.put("Line1", medicines[i][0]);
            item.put("Line2", medicines[i][1]);
            item.put("Line3", medicines[i][2]);
            item.put("Line4", medicines[i][3]);
            item.put("Line5", "Total Cost: " + medicines[i][4] + "/-");
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"Line1", "Line2", "Line3", "Line4", "Line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        listView.setAdapter(sa);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(BuyMedicineActivity.this, BuyMedicineDetailsActivity.class);
            it.putExtra("text1", medicines[i][0]);
            it.putExtra("text2", medicine_details[i]);
            it.putExtra("text3", medicines[i][4]);
            startActivity(it);
        });
    }
}