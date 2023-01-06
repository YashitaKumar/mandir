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
    ArrayList<Integer> selected;
    static int pos=0;
    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<Integer> getSelected() {
        return selected;
    }

    public MyAdapter(@NonNull FirebaseRecyclerOptions<MainGods> options) {

        super(options);
        selected = new ArrayList<>();
        pos=0;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainGods model) {
        holder.textView.setText(model.getGod());
//        Glide.with(holder.circleImageView.getContext()).load(model.getGodName()).into(holder.circleImageView);
        holder.pos = pos;
        pos+=1;
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(holder.checkBox.isChecked())
                {
                    selected.add(holder.pos);
                }
                else
                {
                    holder.textView.setText(model.getGod());
                }
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
       CheckBox checkBox;
       TextView textView;
       CircleImageView circleImageView;
       int pos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = itemView.findViewById(R.id.checkBox);
            textView = itemView.findViewById(R.id.godName);
//            circleImageView = itemView.findViewById(R.id.god);

        }
    }

}
