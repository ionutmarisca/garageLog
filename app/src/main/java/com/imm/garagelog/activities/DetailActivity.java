package com.imm.garagelog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.imm.garagelog.R;
import com.imm.garagelog.adapters.RecyclerAdapter;
import com.imm.garagelog.domain.Car;
import com.imm.garagelog.repository.Repository;

/**
 * Created by Ionut on 7/11/2017.
 */

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        final EditText brandName = (EditText)findViewById(R.id.brandName);
        final EditText modelName = (EditText)findViewById(R.id.modelName);
        final EditText engineSize = (EditText)findViewById(R.id.engineSize);

        Button saveButton = (Button)findViewById(R.id.saveButton);

        Intent intent = getIntent();
        final Car car = (Car) intent.getSerializableExtra("Car");

        brandName.setText(car.getBrand());
        modelName.setText(car.getModel());
        engineSize.setText(String.valueOf(car.getEngineSize()));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                car.setBrand(brandName.getText().toString());
                car.setModel(modelName.getText().toString());
                car.setEngineSize(Integer.parseInt(engineSize.getText().toString()));

                MainActivity.repository.updateCar(car.getId() - 1, car);

                Intent returnIntent = new Intent();
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
