package com.imm.garagelog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imm.garagelog.R;
import com.imm.garagelog.domain.Car;

/**
 * Created by Ionut on 16/12/2017.
 */

public class AddActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity);

        final EditText brandName = (EditText) findViewById(R.id.brandName);
        final EditText modelName = (EditText) findViewById(R.id.modelName);
        final EditText brandLogoUrl = (EditText) findViewById(R.id.brandLogoUrl);
        final EditText engineSize = (EditText) findViewById(R.id.engineSize);

        Button addButton = (Button) findViewById(R.id.addButton);

        final Car car = new Car();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(brandName.getText().toString().equals("") || modelName.getText().toString().equals("") || brandLogoUrl.getText().toString().equals("") || engineSize.getText().toString().equals(""))) {
                    try {
                        car.setBrand(brandName.getText().toString());
                        car.setModel(modelName.getText().toString());
                        car.setBrandLogoUrl(brandLogoUrl.getText().toString());
                        car.setEngineSize(Integer.parseInt(engineSize.getText().toString()));

                        MainActivity.repository.addCar(car);

                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(AddActivity.this, "The value for engine size must be an integer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AddActivity.this, "The fields above cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
