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

public class HeathArticleActivity extends AppCompatActivity {

    private  String[][] healthArticle =
            {
                    {"Benefits of a plant-based diet","","","","Click More Details"},
                    {"Mental health strategies for stress management","","","","Click More Details"},
                    {"Importance of sleep for overall wellness","","","","Click More Details"},
                    {"Exercise routines for beginners","","","","Click More Details"},
                    {"Understanding gut health and probiotics","","","","Click More Details"},
                    {"Tips for maintaining heart health","","","","Click More Details"},
                    {"The role of hydration in physical performance","","","","Click More Details"},
                    {"Managing anxiety through mindfulness practices","","","","Click More Details"},
                    {"Nutritional guide for boosting immunity","","","","Click More Details"},
                    {"The impact of screen time on eye health","","","","Click More Details"}

            };
    private int[] image = {
            R.drawable.diet,
            R.drawable.stress,
            R.drawable.sleep,
            R.drawable.exercise,
            R.drawable.gut,
            R.drawable.heart,
            R.drawable.hydration,
            R.drawable.anxiety,
            R.drawable.nutrition,
            R.drawable.screen
    };
    HashMap<String,String> item;
    ArrayList list;
    SimpleAdapter sa;
    Button btnBack;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_heath_article);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        lst = findViewById(R.id.listViewHA);
        btnBack = findViewById(R.id.buttonHABack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HeathArticleActivity.this,Home_Activity.class));
            }
        });

        list  = new ArrayList();
        for(int i = 0;i<healthArticle.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", healthArticle[i][0]);
            item.put("line2", healthArticle[i][1]);
            item.put("line3", healthArticle[i][2]);
            item.put("line4", healthArticle[i][3]);
            item.put("line5", healthArticle[i][4]);
            list.add(item);

        }
        sa  = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1","line2","line3","line4","line5"},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});

        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it  = new Intent(HeathArticleActivity.this, HealthArticleDetailActivity.class);
                it.putExtra("text1",healthArticle[i][0]);
                it.putExtra("text2",image[i]);
                startActivity(it);
            }
        });

    }
}