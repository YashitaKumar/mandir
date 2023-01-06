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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;

import adapter.MyAdapter;
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
        FirebaseRecyclerOptions<MainGods> options =
                new FirebaseRecyclerOptions.Builder<MainGods>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/pics"), MainGods.class)
                        .build();
        myAdapter = new MyAdapter(options);
        myAdapter.setContext(getApplicationContext());
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

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}