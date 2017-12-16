package com.imm.garagelog.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imm.garagelog.R;
import com.imm.garagelog.adapters.RecyclerAdapter;
import com.imm.garagelog.domain.Car;
import com.imm.garagelog.repository.Repository;
import com.imm.garagelog.utils.Database;

public class MainActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener, RecyclerAdapter.OnItemLongClickListener {
    public static Repository repository;
    RecyclerView myRecyclerView;
    RecyclerView.Adapter myRecyclerAdapter;
    DatabaseReference myRef;
    RecyclerView.LayoutManager myRecyclerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputField = (EditText) findViewById(R.id.carId);
        final Button addCar = (Button) findViewById(R.id.addButton);
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final Button signOutButton = (Button) findViewById(R.id.signOutButton);

        FirebaseDatabase database = Database.getDatabase();
        myRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        repository = new Repository();

        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Repository.class) != null)
                    repository.setCarList(dataSnapshot.getValue(Repository.class).getCarList());
                myRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        myRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        myRecyclerAdapter = new RecyclerAdapter(repository, this, this);
        myRecyclerManager = new LinearLayoutManager(this);

        myRecyclerView.setLayoutManager(myRecyclerManager);
        myRecyclerView.setHasFixedSize(true);
        myRecyclerView.setAdapter(myRecyclerAdapter);

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, 1);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int carId = Integer.parseInt(inputField.getText().toString());

                    if (carId > 0 && carId <= repository.getCarList().size()) {
                        Car car = repository.getCar(carId - 1);

                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Details about " + car.getBrand() + " " + car.getModel());
                        intent.putExtra(Intent.EXTRA_TEXT, "Engine size for this car is: " + car.getEngineSize());

                        startActivity(Intent.createChooser(intent, "Send car details"));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Car ID provided!", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException numberException) {
                    Toast.makeText(getApplicationContext(), "Invalid Car ID provided!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
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
    public void onItemLongClick(final int id) {
        Toast.makeText(getApplicationContext(), "Long click registered", Toast.LENGTH_SHORT).show();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        repository.deleteCar(id);
                        myRecyclerAdapter.notifyDataSetChanged();
                        myRef.setValue(repository);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setMessage("Are you sure to delete this car?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                myRecyclerAdapter.notifyDataSetChanged();
                myRef.setValue(repository);
            }
        }
    }
}
