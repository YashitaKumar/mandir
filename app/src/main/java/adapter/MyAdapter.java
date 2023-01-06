package adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import java.util.Collections;

import de.hdodenhof.circleimageview.CircleImageView;
import model.GodImages;
import model.MainGods;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<MainGods> mainGods;
    Context context;
    int pos =0;

    public MyAdapter(ArrayList<MainGods> mainGods, Context context) {
        this.mainGods = mainGods;
        this.context = context;
        selected = new ArrayList<>();
    }

    ArrayList<Integer> selected;

    public ArrayList<Integer> getSelected() {
        return selected;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            holder.textView.setText(mainGods.get(holder.getBindingAdapterPosition()).getGodMainName());

        Glide.with(holder.circleImageView.getContext()).load(mainGods.get(holder.getBindingAdapterPosition()).getGodName()).into(holder.circleImageView);
        holder.pos = pos;
        pos += 1;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (holder.checkBox.isChecked()) {
                    selected.add(holder.pos);
                } else {
                    selected.remove(selected.indexOf(holder.pos));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mainGods.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       CheckBox checkBox;
       TextView textView;
       CircleImageView circleImageView;
       int pos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.godName);
            circleImageView = itemView.findViewById(R.id.god);

        }
    }

}
