package com.test.e_passportreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class ScanViewResultActivity extends AppCompatActivity {
    EditText ID,Firstname,Surname,Sex,DOB,PassType,ExpDate,Nation;
    Button comfirmButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_view_result);
        ID = (EditText)findViewById(R.id.textViewID);
        Firstname = (EditText)findViewById(R.id.textViewFirstName);
        Surname = (EditText)findViewById(R.id.textViewSurname);
        Sex = (EditText)findViewById(R.id.textViewSex);
        DOB = (EditText)findViewById(R.id.textViewDOB);
        PassType = (EditText)findViewById(R.id.textViewPassportType);
        ExpDate = (EditText)findViewById(R.id.textViewExpDate);
        Nation = (EditText)findViewById(R.id.textViewNation);

        comfirmButton = (Button)findViewById(R.id.buttonConfirm);
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            HashMap<String, String> result = (HashMap<String, String>) intent.getSerializableExtra(Constant.SCAN_RESULT_DATA);
            //Toast.makeText(this, "" + result.size(), Toast.LENGTH_SHORT).show();
            Log.w("RESULT SIZE : " , ""+result.size());
            ID.setText(result.get("Document Number").toString());
            Firstname.setText(result.get("Given Name").toString());
            Surname.setText(result.get("Surname").toString());
            Sex.setText(result.get("Sex").toString());
            DOB.setText(result.get("Date of Birth").toString());
            PassType.setText(result.get("Document Type").toString());
            ExpDate.setText(result.get("Expiration Date").toString());
            Nation.setText(result.get("Nationality").toString());

        }
        comfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ScanViewResultActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
