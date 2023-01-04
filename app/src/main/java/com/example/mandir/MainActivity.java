package com.example.mandir;

import static com.example.mandir.R.drawable.btn_bg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import adapter.FlowersAdapter;
import adapter.MyAdapter;
import de.hdodenhof.circleimageview.CircleImageView;
import model.GodImages;
import model.MainGods;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements ConfettoGenerator {

    //Views and Layouts on screen
    ImageView gImage,centerBell,thali;
    ConstraintLayout constraintLayout;
    SwipeListener swipeListener;
    MotionLayout motionLayout;
    ImageButton btnF,btnS,btnP,btnPremium;

    RecyclerView recyclerView,recyclerViewFlowers;
    GifImageView gifImageView;
    protected ViewGroup container;

   //Other Variables for working
    int i=0;
    int j=0;
    int count,maxCount,btnCount=0;
    MyAdapter myAdapter;
    private int size;
    private int velocitySlow, velocityNormal;
    private Bitmap bitmap;
    float xDown,yDown;
    int []arrImages;
    String []arrNames;
    FlowersAdapter flowersAdapter;
    BottomSheetBehavior bottomSheetBehavior;
    ArrayList<String> godNamess;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //views on layout
        gImage = findViewById(R.id.test);
        constraintLayout = findViewById(R.id.viewForSwipe);
        motionLayout = findViewById(R.id.motionLayout);
        centerBell = findViewById(R.id.imageView5);
        btnF = findViewById(R.id.flowerBtn);
        btnS = findViewById(R.id.shankhBtn);
        btnP = findViewById(R.id.poojaBtn);
        btnPremium = findViewById(R.id.premuimBtn);
        container = findViewById(R.id.container);
        thali = findViewById(R.id.thali);
        recyclerView = findViewById(R.id.navigation);
        gifImageView = findViewById(R.id.thaliFire);
        recyclerViewFlowers = findViewById(R.id.flowersSelection);

        //sound Players
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        final MediaPlayer mediaPlayer2 = MediaPlayer.create(this, R.raw.shankh);
        final MediaPlayer mediaPlayer3 = MediaPlayer.create(this, R.raw.temple);

        //bells on click animations
        centerBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count=0;
                maxCount=3;

            }
        });
        MotionLayout.TransitionListener transitionListener = new MotionLayout.TransitionListener() {

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                mediaPlayer.start();
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if(count<=maxCount)
                {
                    motionLayout.transitionToStart();
                }
                count+=1;
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {
                if(triggerId==1)
                {
                    motionLayout.transitionToStart();
                }

            }
        };
        motionLayout.setTransitionListener(transitionListener);

        //drag and move Thali
        thali.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        xDown = motionEvent.getX();
                        yDown = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float movedX,movedY;
                        movedX = motionEvent.getX();
                        movedY = motionEvent.getY();
                        float distanceX = movedX-xDown;
                        float distanceY = movedY - yDown;
                        thali.setX(thali.getX()+distanceX);
                        thali.setY(thali.getY()+distanceY);
                        gifImageView.setX(gifImageView.getX()+distanceX);
                        gifImageView.setY(gifImageView.getY()+distanceY);

                        break;
                }
                return true;
            }
        });

        //ALL BUTTONS WORKING

        //flowers dropping

        //Flower Selection Working
        arrImages = new int[]{ R.drawable.f4,R.drawable.rose,R.drawable.f5,R.drawable.f6,R.drawable.f7,R.drawable.f8,R.drawable.f9,R.drawable.f10,R.drawable.f11,R.drawable.f12};
        arrNames = new String[]{"Parijat","Rose","neelkamal","mogra","lotus","kovidar","gudhal","mogra","champa","ashok"};
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewFlowers.setLayoutManager(layoutManager);
        flowersAdapter = new FlowersAdapter(arrImages,arrNames);
        flowersAdapter.setContext(getApplicationContext());
        flowersAdapter.setContainer(container);
        flowersAdapter.setRes(getResources());
        recyclerViewFlowers.setAdapter(flowersAdapter);
        recyclerViewFlowers.setHasFixedSize(true);

        //flower dropping button
        btnPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior = BottomSheetBehavior.from(recyclerViewFlowers);
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                    drawable.setTint(getResources().getColor(R.color.gold_med));
                }
                else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                    drawable.setTint(getResources().getColor(R.color.gold_light));
                }

            }
        });
        btnF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Drawable drawable = btnF.getBackground();

                    final Resources res = getResources();
                    size = 156;
                    velocitySlow = 50;
                    velocityNormal = 100;

                    bitmap = Bitmap.createScaledBitmap(
                            BitmapFactory.decodeResource(res, arrImages[0]),
                            size,
                            size,
                            false
                    );
                    ConfettiManager confettiManager = getConfettiManager().setNumInitialCount(0)
                            .setEmissionDuration(3000)
                            .setEmissionRate(20)
                            .animate();



            }
        });

        //Shankh sound Button
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer2.start();

            }
        });

        //pooja button
        btnP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = new MyAnimation(thali, 200);
                anim.setDuration(5000);
                anim.setRepeatCount(3);
                thali.startAnimation(anim);
                gifImageView.startAnimation(anim);
                mediaPlayer2.start();
                final Resources res = getResources();
                size = 156;
                velocitySlow = 50;
                velocityNormal = 100;
                mediaPlayer3.start();
                bitmap = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(res, R.drawable.p),
                        size,
                        size,
                        false
                );
                ConfettiManager confettiManager = getConfettiManager().setNumInitialCount(0)
                        .setEmissionDuration(3000)
                        .setEmissionRate(20)
                        .animate();
                count=0;
                maxCount = 7;
                motionLayout.transitionToState(R.id.end);


            }

        });


        //MAIN MANDIR FUNCTIONING
        //God Images working
        swipeListener = new SwipeListener(constraintLayout);
        //Adapter initialization

        godNamess = new ArrayList<>();

        //Getting Data From Database
        final ArrayList<MainGods> gods = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("/pics").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data: snapshot.getChildren())
                {
                    String name = data.child("godName").getValue().toString();
                    godNamess.add(name);
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
                swipeListener.getData(gods);


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //Navigation Working
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager2);
        FirebaseRecyclerOptions<MainGods> options =
                new FirebaseRecyclerOptions.Builder<MainGods>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/pics"), MainGods.class)
                        .build();
        myAdapter = new MyAdapter(options);
        myAdapter.setMainImage(gImage);
        myAdapter.setContext(getApplicationContext());
        recyclerView.setAdapter(myAdapter);
        recyclerView.setHasFixedSize(true);





    }
    //Extra methods and classes
    @Override
    public Confetto generateConfetto(Random random) {
        return new BitmapConfetto(bitmap);
    }

    private ConfettiManager getConfettiManager() {
        final ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);
        return new ConfettiManager(this, this, source, container)
                .setVelocityX(0, velocitySlow)
                .setVelocityY(velocityNormal, velocitySlow)
                .setRotationalVelocity(180, 90)
                .setTouchEnabled(true);
    }

    public class SwipeListener implements View.OnTouchListener{

        GestureDetector gestureDetector;
        ArrayList<MainGods> mainGods;
        public void getData(ArrayList<MainGods> mg)
        {

            mainGods = new ArrayList<>(mg);
            //initial
            Glide.with(gImage.getContext()).load(mainGods.get(0).getGodImages().get(0).getImage()).into(gImage);
        }

        public SwipeListener(View view) {
            int threshold = 0;
            int velocity_threshold= 0;


            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    float xDiff = e2.getX() - e1.getX();
                    float yDiff = e2.getY() - e1.getY();

                    if(Math.abs(xDiff) > Math.abs(yDiff))
                    {
                        if(Math.abs(xDiff)>threshold && Math.abs(velocityX)> velocity_threshold){
                            if(xDiff>0){
                                //Swipe Right
                                if(i!=0)
                                {
                                    i-=1;
                                }
                                else
                                {
                                    i=mainGods.size()-1;
                                }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);

                            }
                            else
                            {
                                //Swipe Left
                        i+=1;
                        if(i>=mainGods.size())
                        {
                            i=0;
                        }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);

                            }
                        }
                    }
                    else
                    {
                        if (Math.abs(yDiff) > threshold && Math.abs(velocityY) > velocity_threshold)
                        {
                            if(yDiff>0)
                            {
                                //Swipe Down
                        if(j!=0)
                        {
                            j-=1;
                        }
                        else
                        {
                            j=mainGods.get(0).getGodImages().size()-1;
                        }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);
                                myAdapter.setPos(j);
                            }
                            else{
                                //Swipe Up
                                j+=1;
                        if(j>=mainGods.get(0).getGodImages().size())
                        {
                            j=0;
                        }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);
                                myAdapter.setPos(j);
                            }
                        }
                    }

                    return false;
                }
            };
            gestureDetector = new GestureDetector(listener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
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