package com.example.mandir;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import adapter.MyAdapter;
import model.GodImages;
import model.MainGods;

public class DummyActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
        recyclerView = findViewById(R.id.checkk);
        button = findViewById(R.id.ok);
                LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager2);
//        FirebaseRecyclerOptions<MainGods> options =
//                new FirebaseRecyclerOptions.Builder<MainGods>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/pics"), MainGods.class)
//                        .build();

        final ArrayList<MainGods> gods = new ArrayList<>();
        myAdapter = new MyAdapter(gods,getApplicationContext());
        FirebaseDatabase.getInstance().getReference().child("/pics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    String name = data.child("godName").getValue().toString();
                    String key = data.getKey();
                    DataSnapshot dataSnapshot= snapshot.child("/"+key+"/GodImages");
                    ArrayList<GodImages> godImages = new ArrayList<>();
                    for(DataSnapshot data2: dataSnapshot.getChildren())
                    {
                        String poster = data2.child("image").getValue().toString();
                        GodImages godImages1 = new GodImages(poster);
                        godImages.add(godImages1);
                    }
                    MainGods mainGods = new MainGods(name,godImages);
                    gods.add(mainGods);
                }
                myAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> posistions = new ArrayList<>(myAdapter.getSelected());
                Collections.sort(posistions);
                if(posistions.isEmpty())
                {
                    Toast.makeText(DummyActivity.this,"Choose atleast One god",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(DummyActivity.this, posistions.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DummyActivity.this, MainActivity.class);
                    intent.putExtra("pos", posistions);
                    startActivity(intent);
                }
            }
        });



    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        myAdapter.startListening();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        myAdapter.stopListening();
//    }
}