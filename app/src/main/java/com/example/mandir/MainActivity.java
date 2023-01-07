package com.example.mandir;

import static com.example.mandir.R.drawable.btn_bg;
import static com.example.mandir.R.drawable.final_arch;

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
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.net.URI;
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
    ImageView gImage,centerBell,thali,flowerThali,f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11;
    ConstraintLayout constraintLayout,hand,parent,flowerCountLayout;
    SwipeListener swipeListener;
    MotionLayout motionLayout,motionLayoutSide1,motionLayoutSide2;
    ImageButton btnF,btnS,btnP,btnPremium,btn;

    RecyclerView recyclerView,recyclerViewFlowers;
    GifImageView gifImageView;
    protected ViewGroup container;
    TabLayout navigation;
    EditText editText;
    Button confirm;



   //Other Variables for working
    int i=0;
    int j=0;
    int count,maxCount,btnCount=0,count2=0,count3=0;
    MyAdapter myAdapter;
    private int size;
    private int velocitySlow, velocityNormal;
    private Bitmap bitmap;
    float xDown,yDown;
    int []arrImages;
    String []arrNames;
    FlowersAdapter flowersAdapter;
    BottomSheetBehavior bottomSheetBehavior,flowers;
    ArrayList<String> godNamess;
    ArrayList<Integer> position;


    View view;


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
//        recyclerView = findViewById(R.id.navigation);
        gifImageView = findViewById(R.id.thaliFire);
        recyclerViewFlowers = findViewById(R.id.flowersSelection);
        motionLayoutSide2 = findViewById(R.id.motionLayoutSide2);
        motionLayoutSide1 = findViewById(R.id.motionLayoutSide1);
        hand = findViewById(R.id.handAnimation);
        flowerThali = findViewById(R.id.flowerThali);
        f1 = findViewById(R.id.f1);
        f2 = findViewById(R.id.f2);
        f3 = findViewById(R.id.f3);
        f4 = findViewById(R.id.f4);
        f5 = findViewById(R.id.f5);
        f6 = findViewById(R.id.f6);
        f7 = findViewById(R.id.f7);
        f8 = findViewById(R.id.f8);
        f9 = findViewById(R.id.f9);
        f10 = findViewById(R.id.f10);
        f11 = findViewById(R.id.f11);
        parent = findViewById(R.id.constraintLayout7);
        navigation = findViewById(R.id.navigation);
       btn = findViewById(R.id.temp);
//       editText = findViewById(R.id.count);
//       confirm = findViewById(R.id.FlowerCountConfirm);
//       flowerCountLayout = findViewById(R.id.layoutSelection);
       btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               SharedPreferences sharedPreferences= getSharedPreferences("PREFERENCES",MODE_PRIVATE);
               SharedPreferences.Editor editor = sharedPreferences.edit();
               editor.putString("FirstTimeInstall","Edit");
               editor.apply();
               startActivity(new Intent(MainActivity.this,DummyActivity.class));
           }
       });
       position = (ArrayList<Integer>) PrefConfig.readListFromPref(getApplicationContext());
       if(position==null) {
           position = getIntent().getIntegerArrayListExtra("pos");
       }

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

        //Animation for hand
        hand.setY(1000f);
        hand.animate().translationYBy(-1050f).setDuration(1500);
                    new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    gifImageView.setVisibility(View.VISIBLE);
                    hand.animate().translationYBy(1100f).setDuration(1500);
                }
            },2000);

        //sound Players
        final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.sound);
        final MediaPlayer mediaPlayer2 = MediaPlayer.create(this, R.raw.shankh);
        final MediaPlayer mediaPlayer3 = MediaPlayer.create(this, R.raw.temple);

        //bells on click animations
        count=0;
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
                if(count<=5) {
                    motionLayout.transitionToStart();
                }
                count+=1;
                if(count==7)
                {
                    count=0;
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {


            }
        };
        MotionLayout.TransitionListener transitionListener2 = new MotionLayout.TransitionListener() {

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                mediaPlayer.start();
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if(count2<=5) {
                    motionLayout.transitionToStart();
                }
                count2+=1;
                if(count2==7)
                {
                    count2=0;
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {


            }
        };
        MotionLayout.TransitionListener transitionListener3 = new MotionLayout.TransitionListener() {

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {
                mediaPlayer.start();
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if(count3<=5) {
                    motionLayout.transitionToStart();
                }
                count3+=1;
                if(count3==7)
                {
                    count3=0;
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {


            }
        };
        motionLayout.setTransitionListener(transitionListener);
        motionLayoutSide2.setTransitionListener(transitionListener2);
        motionLayoutSide1.setTransitionListener(transitionListener3);

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
                flowersAdapter.setImageAssets(f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,parent);
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




//                String ct = editText.getText().toString();
//                int countFlower = Integer.parseInt(ct);
//                confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
                        final Resources res = getResources();
                        size = 156;
                        velocitySlow = 50;
                        velocityNormal = 100;

                        bitmap = Bitmap.createScaledBitmap(
                                BitmapFactory.decodeResource(res, R.drawable.p),
                                size,
                                size,
                                false
                        );
                        ConfettiManager confettiManager = getConfettiManager().setNumInitialCount(0)
                                .setEmissionDuration(3000)
                                .setEmissionRate(15)
                                .animate();
                        f1.setVisibility(View.INVISIBLE);
                        f2.setVisibility(View.INVISIBLE);
                        f3.setVisibility(View.INVISIBLE);
                        f4.setVisibility(View.INVISIBLE);
                        f5.setVisibility(View.INVISIBLE);
                        f6.setVisibility(View.INVISIBLE);
                        f7.setVisibility(View.INVISIBLE);
                        f8.setVisibility(View.INVISIBLE);
                        f9.setVisibility(View.INVISIBLE);
                        f10.setVisibility(View.INVISIBLE);
                        f11.setVisibility(View.INVISIBLE);
                        f1.setImageDrawable(getDrawable(R.drawable.p));
                        f2.setImageDrawable(getDrawable(R.drawable.p));
                        f3.setImageDrawable(getDrawable(R.drawable.p));
                        f4.setImageDrawable(getDrawable(R.drawable.p));
                        f5.setImageDrawable(getDrawable(R.drawable.p));
                        f6.setImageDrawable(getDrawable(R.drawable.p));
                        f7.setImageDrawable(getDrawable(R.drawable.p));
                        f8.setImageDrawable(getDrawable(R.drawable.p));
                        f9.setImageDrawable(getDrawable(R.drawable.p));
                        f10.setImageDrawable(getDrawable(R.drawable.p));
                        f11.setImageDrawable(getDrawable(R.drawable.p));
                        Transition transition = new Fade();
                        transition.setDuration(20000);
                        transition.addTarget(R.id.f1);
                        transition.addTarget(R.id.f2);
                        transition.addTarget(R.id.f3);
                        transition.addTarget(R.id.f4);
                        transition.addTarget(R.id.f5);
                        transition.addTarget(R.id.f6);
                        transition.addTarget(R.id.f7);
                        transition.addTarget(R.id.f8);
                        transition.addTarget(R.id.f9);
                        transition.addTarget(R.id.f10);
                        transition.addTarget(R.id.f11);
                        TransitionManager.beginDelayedTransition(parent, transition);
                        f1.setVisibility(View.VISIBLE);
                        f2.setVisibility(View.VISIBLE);
                        f3.setVisibility(View.VISIBLE);
                        f4.setVisibility(View.VISIBLE);
                        f5.setVisibility(View.VISIBLE);
                        f6.setVisibility(View.VISIBLE);
                        f7.setVisibility(View.VISIBLE);
                        f8.setVisibility(View.VISIBLE);
                        f9.setVisibility(View.VISIBLE);
                        f10.setVisibility(View.VISIBLE);
                        f11.setVisibility(View.VISIBLE);

//                    }
//                });




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
                motionLayout.transitionToState(R.id.end);
                motionLayoutSide1.transitionToState(R.id.end);
                motionLayoutSide2.transitionToState(R.id.end);
                f1.setVisibility(View.INVISIBLE);
                f2.setVisibility(View.INVISIBLE);
                f3.setVisibility(View.INVISIBLE);
                f4.setVisibility(View.INVISIBLE);
                f5.setVisibility(View.INVISIBLE);
                f6.setVisibility(View.INVISIBLE);
                f7.setVisibility(View.INVISIBLE);
                f8.setVisibility(View.INVISIBLE);
                f9.setVisibility(View.INVISIBLE);
                f10.setVisibility(View.INVISIBLE);
                f11.setVisibility(View.INVISIBLE);
                f1.setImageDrawable(getDrawable(R.drawable.p));
                f2.setImageDrawable(getDrawable(R.drawable.p));
                f3.setImageDrawable(getDrawable(R.drawable.p));
                f4.setImageDrawable(getDrawable(R.drawable.p));
                f5.setImageDrawable(getDrawable(R.drawable.p));
                f6.setImageDrawable(getDrawable(R.drawable.p));
                f7.setImageDrawable(getDrawable(R.drawable.p));
                f8.setImageDrawable(getDrawable(R.drawable.p));
                f9.setImageDrawable(getDrawable(R.drawable.p));
                f10.setImageDrawable(getDrawable(R.drawable.p));
                f11.setImageDrawable(getDrawable(R.drawable.p));
                Transition transition = new Fade();
                transition.setDuration(30000);
                transition.addTarget(R.id.f1);
                transition.addTarget(R.id.f2);
                transition.addTarget(R.id.f3);
                transition.addTarget(R.id.f4);
                transition.addTarget(R.id.f5);
                transition.addTarget(R.id.f6);
                transition.addTarget(R.id.f7);
                transition.addTarget(R.id.f8);
                transition.addTarget(R.id.f9);
                transition.addTarget(R.id.f10);
                transition.addTarget(R.id.f11);
                TransitionManager.beginDelayedTransition(parent, transition);
                f1.setVisibility(View.VISIBLE);
                f2.setVisibility(View.VISIBLE);
                f3.setVisibility(View.VISIBLE);
                f4.setVisibility(View.VISIBLE);
                f5.setVisibility(View.VISIBLE);
                f6.setVisibility(View.VISIBLE);
                f7.setVisibility(View.VISIBLE);
                f8.setVisibility(View.VISIBLE);
                f9.setVisibility(View.VISIBLE);
                f10.setVisibility(View.VISIBLE);
                f11.setVisibility(View.VISIBLE);


            }

        });




        //Navigation Working
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
//        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
//        recyclerView.setLayoutManager(layoutManager2);
//        FirebaseRecyclerOptions<MainGods> options =
//                new FirebaseRecyclerOptions.Builder<MainGods>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("/pics"), MainGods.class)
//                        .build();
//        myAdapter = new MyAdapter(options);
//        myAdapter.setMainImage(gImage);
//        myAdapter.setContext(getApplicationContext());
//        recyclerView.setAdapter(myAdapter);
//        recyclerView.setHasFixedSize(true);







    }
    //Extra methods and classes
    @Override
    public Confetto generateConfetto(Random random) {
        return new BitmapConfetto(bitmap);
    }

    private ConfettiManager getConfettiManager() {
        final ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);
        Rect rect = new Rect(container.getLeft(),container.getTop(),container.getRight(),flowerThali.getBottom());
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

//            mainGods = new ArrayList<>(mg);
            mainGods = new ArrayList<>();
            int p=0,m=0;
            while((p!=position.size())&&(m!=mg.size()))
            {
                if(position.get(p)==m)
                {
                    mainGods.add(mg.get(m));
                    p+=1;
                    m+=1;
                }
                else
                {
                    m+=1;
                }
            }
                            for(int i=0;i<mainGods.size();i++)

                {
                    CircleImageView imageView = new CircleImageView(MainActivity.this);
                    ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(100, 100);

                    imageView.setLayoutParams(layoutParams);
                    Glide.with(imageView.getContext()).load(mainGods.get(i).getGodName()).into(imageView);
                    navigation.addTab(navigation.newTab().setCustomView(imageView));
                }
            navigation.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int positionNav = navigation.getSelectedTabPosition();
                    Glide.with(gImage.getContext()).load(mainGods.get(positionNav).getGodImages().get(j).getImage()).into(gImage);

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

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
                            if(mainGods.isEmpty()==false)
                                j=mainGods.get(0).getGodImages().size()-1;
                            else
                                j=0;
                        }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);

                            }
                            else{
                                //Swipe Up
                                j+=1;
                        if(j>=mainGods.get(0).getGodImages().size())
                        {
                            j=0;
                        }
                                Glide.with(gImage.getContext()).load(mainGods.get(i).getGodImages().get(j).getImage()).into(gImage);

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



}