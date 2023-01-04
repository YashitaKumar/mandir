package adapter;

import android.content.Context;
import android.os.Build;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mandir.MainActivity;
import com.example.mandir.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.GodImages;
import model.MainGods;

public class MyAdapter extends FirebaseRecyclerAdapter<MainGods, MyAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    ImageView mainImage;
    Context context;
    View view;
    Animation animation;

    public void setView(View view) {
        this.view = view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    int pos = 0;

    public void setMainImage(ImageView mainImage) {
        this.mainImage = mainImage;
    }

    public MyAdapter(@NonNull FirebaseRecyclerOptions<MainGods> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainGods model) {
        Glide.with(holder.imageView.getContext()).load(model.getGodName()).into(holder.imageView);
        animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.scaling);
        setView(holder.itemView);
        holder.itemView.setAnimation(animation);
//        SwipeListener swipeListener = new SwipeListener(holder.itemView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("/pics").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String name = data.child("godName").getValue().toString();
                            String key = data.getKey();
                            if (name.equals(model.getGodName())) {
                                DataSnapshot dataSnapshot = snapshot.child("/" + key + "/GodImages");
                                ArrayList<GodImages> godImages = new ArrayList<>();
                                for (DataSnapshot data2 : dataSnapshot.getChildren()) {
                                    String poster = data2.child("image").getValue().toString();
                                    GodImages godImages1 = new GodImages(poster);
                                    godImages.add(godImages1);
                                }
                                Glide.with(mainImage.getContext()).load(godImages.get(pos).getImage()).into(mainImage);

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nav);
        }
    }

//    public class SwipeListener implements View.OnTouchListener {
//
//        GestureDetector gestureDetector;
//        ArrayList<MainGods> mainGods;
//
//        public SwipeListener(View view) {
//            int threshold = 0;
//            int velocity_threshold = 0;
//
//
//            GestureDetector.SimpleOnGestureListener listener = new GestureDetector.SimpleOnGestureListener() {
//
//                @Override
//                public boolean onDown(MotionEvent e) {
//                    return true;
//                }
//
//                @Override
//                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                    float xDiff = e2.getX() - e1.getX();
//                    float yDiff = e2.getY() - e1.getY();
//
//                    if (Math.abs(xDiff) > Math.abs(yDiff)) {
//                        if (Math.abs(xDiff) > threshold && Math.abs(velocityX) > velocity_threshold) {
//                            if (xDiff > 0) {
//                                //Swipe Right
//                                view.startAnimation(animation);
//
//
//
//                            } else {
//                                view.startAnimation(animation);
//                            }
//                        }
//                    } else {
//
//                    }
//
//                    return false;
//                }
//            };
//            gestureDetector = new GestureDetector(listener);
//            view.setOnTouchListener(this);
//        }
//
//        @Override
//        public boolean onTouch(View view, MotionEvent motionEvent) {
//            return gestureDetector.onTouchEvent(motionEvent);
//        }
//    }
}
