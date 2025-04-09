package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {
    private String[][] doctor_details1 =
            {
                    {"Doctor Name : Dr. Manoj Kishor Chhotray","Hospital Address : Apollo Hospitals, Gajapati Nagar, Bhubaneswar", "Experience : 54 years", "Mobile number = +91 9876543210", "1000"},
                    {"Doctor Name : Dr. Amitav Mohanty", "Hospital Address : Apollo Hospitals, Gajapati Nagar, Bhubaneswar", "Experience : 36 years", "Mobile number = +91 8765432109", "1000"},
                    {"Doctor Name : Dr. Debashis Maikap","Hospital Address : Kalinga Institute Of Medical Sciences, Patia, Bhubaneswar", "Experience : 14 years", "Mobile number = +91 9988776655", "800"},
                    {"Doctor Name : Dr. Salil Kumar Parida","Hospital Address : Apollo Hospitals, Gajapati Nagar, Bhubaneswar", "Experience : 22 years", "Mobile number = +91 7654321098","600"},
                    {"Doctor Name : Dr. S. Hari Sankara Patra", "Hospital Address : Kalinga Hospital, Bhubaneswar", "Experience : 16 years","Mobile number = +91 9123456789","1000"},
                    {"Doctor Name : Dr. Samarendra Behera", "Hospital Address : Kalinga Hospital, Bhubaneswar", "Experience : 8 years","Mobile number = +91 8547692301","1000"},
                    {"Doctor Name : Dr. Rangadhar Satapathy", "Hospital Address : Multicare Homeopathy Clinic, Chandrasekharpur, Bhubaneswar","Experience : 16 years","Mobile number = +91 7894561230","400"},
                    {"Doctor Name : Dr. Sanjeev Patnaik", "Hospital Address : Kalinga Hospital, Bhubaneswar	", "Experience : 23 years","Mobile number = +91 6234578901","1000"},
                    {"Doctor Name : Dr. Jagannath Sahoo", "Hospital Address : IMS & Sum Hospital, Bhubaneswar ", "Experience : 9 years","Mobile number = +91 9356789012","1000"},
                    {"Doctor Name : Dr. Bishnu Prasad Patro	", "Hospital Address : IMS & Sum Hospital, Bhubaneswar ","Experience : 17 years","Mobile number = +91 8012345678","1000"},
                    {"Doctor Name : Dr. Shivabrata Dhal Mohapatra", "Hospital Address : Kalinga Hospital, Bhubaneswar","Experience : 6 years","Mobile number = +91 7209865432","800"},

            };
    private String[][] doctor_details2 =
            {
                    {"Doctor Name : Dr. Sunita Sahoo", "Hospital Address : Restora HealthCare, Gajapati Nagar, Bhubaneswer", "Experience : 19 years", "Mobile number = +91 9876543210", "1000"},
                    {"Doctor Name : Dt. Swati Mohapatra", "Hospital Address : Swati Mohapatra Dietician Clinic, Khandagiri, Bhubaneswer", "Experience : 19 years","Mobile number = +91 8765432109", "850"},
                    {"Doctor Name : Dt. Trupti Padhi", "Hospital Address :Dt. Trupti's Diet Consultancy, Chandrasekharpur, Bhubaneswer", "Experience : 23 years","Mobile number = +91 9988776655", "500"},
                    {"Doctor Name : Dt. Ankita Chowdhury Nandi", "Hospital Address : The Wellness Jar, Dumuduma, Bhubaneswer", "Experience : 14 years","Mobile number = +91 7654321098", "300"},
                    {"Doctor Name : Dt. Anmayee Nanda", "Hospital Address : Hi-Tech Medical College and Hospital, Bhubaneswer", "Experience : 14 years", "Mobile number = +91 9123456789","350"},
                    {"Doctor Name : Dt. Suryasikha Mohanty", "Hospital Address : Dt. Suryasikha Mohanty's Clinic, Kalinga Nagar, Bhubaneswer", "Experience : 14","Mobile number = +91 8547692301","600"},
                    {"Doctor Name : Dr. Priyambada Sahoo", "Hospital Address : Apollo Sugar Clinics, Bhubaneshwer", "Experience : 10+ years","Mobile number = +91 7894561230", "500"},
                    {"Doctor Name : Mr. Ajit Kumar Prusty", "Hospital Address : Newlife Wellness Centre, Nageswar Tangi, Bhubaneswer", "Experience : 12 years","Mobile number = +91 6234578901","200"}
            };
    private String[][] doctor_details3 =
            {
                    {"Doctor Name: Dr. Smruti Ranjan Behera", "Hospital Address: Hi-Tech Dental College & Hospital, Pandara, Bhubaneswar","Experience: 15 years","Mobile number = +91 9345678901","800"},
                    {"Doctor Name: Dr. Satyajit Dash", "Hospital Address: Kalinga Institute of Dental Sciences, Patia, Bhubaneswar","Experience: 12 years","Mobile number = +91 8213456789","700"},
                    {"Doctor Name: Dr. Anuradha Pati", "Hospital Address: Dental Spa, Saheed Nagar, Bhubaneswar","Experience: 10 years","Mobile number = +91 7564891230","600"},
                    {"Doctor Name: Dr. Debashis Mishra", "Hospital Address: The Dental Clinic, Nayapalli, Bhubaneswar","Experience: 8 years","Mobile number = +91 6897543201","500"},
                    {"Doctor Name: Dr. Priyadarshini Nayak", "Hospital Address: Smile Dental Care, Jayadev Vihar, Bhubaneswar","Experience: 7 years","Mobile number = +91 9078563412","550"},
                    {"Doctor Name: Dr. Subrat Kumar Sahoo", "Hospital Address: Dent-O-Care, Kharabela Nagar, Bhubaneswar","Experience: 6 years","Mobile number = +91  8456732190","450"},
                    {"Doctor Name: Dr. Rajashree Mohanty", "Hospital Address: Happy Smile Dental Clinic, Chandrasekharpur, Bhubaneswar","Experience: 5 years","Mobile number = +91 7689054321","400"},
                    {"Doctor Name: Dr. Ankit Kumar", "Hospital Address: Sparkle Dental Studio, Patrapada, Bhubaneswar","Experience: 4 years","Mobile number = +91 9821346578","350"}
            };
    private String[][] doctor_details4 =
            {
                    {"Doctor Name: Dr. Jagadananda Mishra", "Hospital Address: SUM Hospital, K-8, Kalinga Nagar, Ghatikia, Bhubaneswar", "Experience: 8years","Mobile number = +91 7986543210","800"},
                    {"Doctor Name: Dr. Prakash Kumar Sahoo", "Hospital Address: SUM Hospital, K-8, Kalinga Nagar, Ghatikia, Bhubaneswar", "Experience: 10years","Mobile number = +91 6897451203","700"},
                    {"Doctor Name: Dr. Deepak Kumar Benia", "Hospital Address: IMS & SUM Hospital, K-8, Kalinga Nagar, Ghatikia, Bhubaneswar", "Experience: 5 years","Mobile number = +91 9201345678","1,000"},
                    {"Doctor Name: Dr. Kishan Kishan", "Hospital Address: IMS & SUM Hospital, K-8, Kalinga Nagar, Ghatikia, Bhubaneswar", "Experience: 14 years","Mobile number = +91 8754903126","1,100"},
                    {"Doctor Name: Dr. Pradeep Kumar Jena", "Hospital Address: KIMS Hospital, Baramunda, Bhubaneswar", "Experience: 22 year","Mobile number = +91 7012349856","1,200"},
                    {"Doctor Name: Dr. Satyaprakash Mishra", "Hospital Address: KIMS Hospital, Baramunda, Bhubaneswar", "Experience: 18 years","Mobile number = +91 9685742031","900"},
                    {"Doctor Name: Dr. Rajeev Kumar", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar", "Experience: 20 years","Mobile number = +91 8347216590","1,500"},
                    {"Doctor Name: Dr. Nidhi Rathi", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar", "Experience: 15 years","Mobile number = +91 7563021984","1,000"},
                    {"Doctor Name: Dr. Suman K. Behera", "Hospital Address: Kalinga Institute of Medical Sciences (KIMS), Patia, Bhubaneswar", "Experience: 25 years","Mobile number = +91 9154786203","1,100"},
                    {"Doctor Name: Dr. Abhishek Mohapatra", "Hospital Address: Kalinga Hospital, Bhubaneswar", "Experience: 16 years","Mobile number = +91 8421390576","800"}
            };
    private  String[][] doctor_details5 =
            {
                    {"Doctor Name: Dr. Prasant Kumar Sahoo", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar","Experience: 37 years","Mobile number = +91 7896541230", "1,000"},
                    {"Doctor Name: Dr. P C Rath", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar","Experience: 36 years","Mobile number = +91 9087612345", "1,000"},
                    {"Doctor Name: Dr. Byomakesh Dikshit", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar","Experience: 8 years","Mobile number = +91 8765439081", "1,000"},
                    {"Doctor Name: Dr. Brajaraj Das", "Hospital Address: Apollo Hospitals, Gajapati Nagar, Bhubaneswar","Experience: 25 years","Mobile number = +91 7654321876", "1,000"},
                    {"Doctor Name: Dr. Ashutosh Kumar", "Hospital Address: CARE Hospitals, Bhubaneswar","Experience: 17 years","Mobile number = +91 9543206789", "900"},
                    {"Doctor Name: Dr. Bikram Keshari Mohapatra", "Hospital Address: CARE Hospitals, Bhubaneswar","Experience: 10+ years","Mobile number = +91 8321456790", "800"},
                    {"Doctor Name: Dr. Debasish Mohapatra", "Hospital Address: CARE Hospitals, Bhubaneswar","Experience: 10+ years","Mobile number = +91 7012896543", "850"},
                    {"Doctor Name: Dr. Giridhari Jena", "Hospital Address: CARE Hospitals, Bhubaneswar","Experience: 5 years","Mobile number = +91 6245789012", "900"},
                    {"Doctor Name: Dr. Kanhu Charan Mishra", "Hospital Address: CARE Hospitals, Bhubaneswar","Experience: 05+ years","Mobile number = +91 9871203456", "850"},
                    {"Doctor Name: Dr. Sushil Kumar", "Hospital Address: KIMS Hospital, Baramunda, Bhubaneswar","Experience: 15 years","Mobile number = +91 8523679014", "1,000"},
                    {"Doctor Name: Dr. Satyaprakash Mishra", "Hospital Address: KIMS Hospital, Baramunda, Bhubaneswar","Experience: 18 years","Mobile number = +91 9632587410", "1,000"},
                    {"Doctor Name: Dr. Rajeev Kumar", "Hospital Address: SUM Hospital, K-8, Kalinga Nagar, Ghatikia, Bhubaneswar","Experience: 20 years","Mobile number = +91 7418529630", "1,100"}
            };

    TextView tv;
    Button btn;
    String[][] doctor_details = {};
    HashMap<String,String> item;
    ArrayList List;
    SimpleAdapter sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn = findViewById(R.id.buttonODBack);
        tv = findViewById(R.id.textViewODtittle);
        Intent it = getIntent();
        String tittle = it.getStringExtra("tittle");
        tv.setText(tittle);

        if(tittle.compareTo("Family Physician")==0)
            doctor_details = doctor_details1;
        else
        if(tittle.compareTo("Dietician")==0)
            doctor_details = doctor_details2;
        else
        if(tittle.compareTo("Dentist")==0)
            doctor_details = doctor_details3;
        else
        if(tittle.compareTo("Surgeon")==0)
            doctor_details = doctor_details4;
        else
        if(tittle.compareTo("Cardiologist")==0)
            doctor_details = doctor_details5;


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDetailsActivity.this,FindDoctor_Activity.class));
            }
        });
        List = new ArrayList();
        for (int i = 0;i<doctor_details.length;i++){
            item = new HashMap<String,String>();
            item.put("line1", doctor_details[i][0]);
            item.put("line2", doctor_details[i][1]);
            item.put("line3", doctor_details[i][2]);
            item.put("line4", doctor_details[i][3]);
            item.put("line5", "Consultant fee : "+doctor_details[i][4]+"/-rupees");
            List.add(item);
        }
        sa = new SimpleAdapter(this,List,
                R.layout.multi_lines,new String[]{"line1","line2","line3","line4","line5"},
                new int[]{R.id.line_a,R.id.line_b,R.id.line_c,R.id.line_d,R.id.line_e});
        ListView lst = findViewById(R.id.listViewOD);
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(DoctorDetailsActivity.this, Book_Appointment_Activity.class);




                it.putExtra("text5",doctor_details[i][4]);
                startActivity(it);
            }
        });
    }
}