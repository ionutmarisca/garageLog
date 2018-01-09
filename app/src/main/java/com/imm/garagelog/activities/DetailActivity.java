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

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

/**
 * Created by Ionut on 7/11/2017.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        final EditText brandName = (EditText) findViewById(R.id.brandName);
        final EditText modelName = (EditText) findViewById(R.id.modelName);
        final EditText engineSize = (EditText) findViewById(R.id.engineSize);
        final BarChart barStatistics = (BarChart) findViewById(R.id.barStatistics);

        Button saveButton = (Button) findViewById(R.id.saveButton);

        Intent intent = getIntent();
        final Car car = (Car) intent.getSerializableExtra("Car");

        brandName.setText(car.getBrand());
        modelName.setText(car.getModel());
        engineSize.setText(String.valueOf(car.getEngineSize()));

        initializeStatistics(barStatistics, car.getEngineSize());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(brandName.getText().toString().equals("") || modelName.getText().toString().equals("") || engineSize.getText().toString().equals(""))) {
                    try {
                        car.setBrand(brandName.getText().toString());
                        car.setModel(modelName.getText().toString());
                        car.setEngineSize(Integer.parseInt(engineSize.getText().toString()));

                        MainActivity.repository.updateCar(car.getId() - 1, car);

                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    } catch (NumberFormatException e) {
                        Toast.makeText(DetailActivity.this, "The value for engine size must be an integer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DetailActivity.this, "The fields above cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initializeStatistics(BarChart chart, int engineSize) {
        chart.addBar(new BarModel("Your Car", engineSize, 0xFF123456));
        chart.addBar(new BarModel("Overall Cars", MainActivity.repository.getAverageEngineSize(), 0xFF343456));

        chart.startAnimation();
    }
}
