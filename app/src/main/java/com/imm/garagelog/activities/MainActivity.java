package com.imm.garagelog.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    public static Context context;
    RecyclerView myRecyclerView;
    RecyclerView.Adapter myRecyclerAdapter;
    DatabaseReference myRef;
    RecyclerView.LayoutManager myRecyclerManager;

    public static void showRepositoryUpdated() {
        Toast.makeText(context, "Repository updated!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText inputField = (EditText) findViewById(R.id.carId);
        final Button addCar = (Button) findViewById(R.id.addButton);
        final Button sendButton = (Button) findViewById(R.id.sendButton);
        final Button signOutButton = (Button) findViewById(R.id.signOutButton);
        final Button getNotification = (Button) findViewById(R.id.getNotification);
        final NumberPicker numberPicker = (NumberPicker) findViewById(R.id.numberPicker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        FirebaseDatabase database = Database.getDatabase();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        context = getApplicationContext();
        myRef = database.getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        repository = new Repository();

        // Attach a listener to read the data at our posts reference
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Repository.class) != null) {
                    repository.setCarList(dataSnapshot.getValue(Repository.class).getCarList());
                }
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
                if (email == null && repository.getCarList().size() > 0) {
                    Toast.makeText(getApplicationContext(), "As a guest user, you can only have one car added.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, AddActivity.class);
                    startActivityForResult(i, 1);
                }
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

        getNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(v);
            }
        });

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                inputField.setText(Integer.toString(newVal));
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

    public void sendNotification(View view) {

        //Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(android.R.drawable.sym_def_app_icon)
                        .setContentTitle("garageLog")
                        .setContentText("This is a sample notification!");


        // Gets an instance of the NotificationManager service//

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        mNotificationManager.notify(001, mBuilder.build());
    }
}
