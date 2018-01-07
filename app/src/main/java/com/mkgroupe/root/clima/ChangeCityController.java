package com.mkgroupe.root.clima;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ChangeCityController extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_city_layout);
        final EditText mCityReqET = findViewById(R.id.queryET);
        ImageButton mBackB = findViewById(R.id.backButton);
        mBackB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String mCityName = mCityReqET.getText().toString();
                Intent myIntent = new Intent(ChangeCityController.this,MainActivity.class);
                myIntent.putExtra("City",mCityName);
                startActivity(myIntent);
                finish();
            }
        });
    }
}
