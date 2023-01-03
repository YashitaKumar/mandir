package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mandir.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

import model.GodImages;
import model.MainGods;

public class MyAdapter extends FirebaseRecyclerAdapter<MainGods, MyAdapter.MyViewHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;

    public MyAdapter(@NonNull FirebaseRecyclerOptions<MainGods> options, Context context) {
        super(options);
        this.context = context;
    }

    public MyAdapter(@NonNull FirebaseRecyclerOptions<MainGods> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull MainGods model) {
        holder.imageView.setText(model.getGodName());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.navigation,parent,false);
        return new MyAdapter.MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.nav);
        }
    }
}
