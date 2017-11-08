package com.imm.garagelog.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.imm.garagelog.R;
import com.imm.garagelog.adapters.RecyclerAdapter;
import com.imm.garagelog.domain.Car;
import com.imm.garagelog.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {
    RecyclerView myRecyclerView;
    RecyclerView.Adapter myRecyclerAdapter;
    RecyclerView.LayoutManager myRecyclerManager;
    static Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputField = (EditText)findViewById(R.id.carId);
        final Button sendButton = (Button)findViewById(R.id.sendButton);

        repository = new Repository();

        repository.addCar(new Car(1, "Audi", "A4", "http://www.glimport.ch/doc/images/audi.png", 1998));
        repository.addCar(new Car(2, "BMW", "3er Series", "https://vignette.wikia.nocookie.net/forzamotorsport4/images/2/25/BMW_logo.png/revision/latest?cb=20111208164856", 1998));
        repository.addCar(new Car(3, "Mercedes", "C Klasse", "http://pngimg.com/uploads/mercedes_logos/mercedes_logos_PNG20.png", 1998));

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerAdapter = new RecyclerAdapter(repository.getCarList(), this);
        myRecyclerManager = new LinearLayoutManager(this);

        myRecyclerView.setLayoutManager(myRecyclerManager);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int carId = Integer.parseInt(inputField.getText().toString());

                    if(carId > 0 && carId <= repository.getCarList().size()) {
                        Car car = repository.getCar(carId - 1);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Details about " + car.getBrand() + " " + car.getModel());
                        intent.putExtra(Intent.EXTRA_TEXT, "Engine size for this car is: " + car.getEngineSize());

                        startActivity(Intent.createChooser(intent, "Send car details"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Car ID provided!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(NumberFormatException numberException) {
                    Toast.makeText(getApplicationContext(), "Invalid Car ID provided!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(int id) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("Car", repository.getCar(id));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                myRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }
}
