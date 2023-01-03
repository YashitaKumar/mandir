package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mandir.MainActivity;
import com.example.mandir.R;
import com.github.jinatonic.confetti.ConfettiManager;
import com.github.jinatonic.confetti.ConfettiSource;
import com.github.jinatonic.confetti.ConfettoGenerator;
import com.github.jinatonic.confetti.confetto.BitmapConfetto;
import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.Random;

public class FlowersAdapter extends RecyclerView.Adapter<FlowersAdapter.FlowersViewHolder> implements ConfettoGenerator {
    int []arrImages;
    String []arrNames;
    Context context;
    int pos=0;
    private int size;
    private int velocitySlow, velocityNormal;
    private Bitmap bitmap;
    ViewGroup container;
    Resources res;

    public void setRes(Resources res) {
        this.res = res;
    }

    public void setContainer(ViewGroup container) {
        this.container = container;
    }

    public void setContext(Context context) {
        this.context = context;
    }
    public int getPos(){

        return pos;
    }

    public FlowersAdapter(int[] arrImages, String[] arrNames) {
        this.arrImages = arrImages;
        this.arrNames = arrNames;
    }

    @NonNull
    @Override
    public FlowersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.flower_selection,parent,false);
        return new FlowersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlowersViewHolder holder, int position) {
        holder.imageView.setImageResource(arrImages[position]);
        holder.textView.setText(arrNames[position]);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pos=holder.getAdapterPosition();
                size = 156;
                velocitySlow = 50;
                velocityNormal = 100;

                bitmap = Bitmap.createScaledBitmap(
                        BitmapFactory.decodeResource(res, arrImages[pos]),
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

    }

    @Override
    public int getItemCount() {
        return arrImages.length;
    }

    @Override
    public Confetto generateConfetto(Random random) {
        return new BitmapConfetto(bitmap);
    }
    private ConfettiManager getConfettiManager() {
        final ConfettiSource source = new ConfettiSource(0, -size, container.getWidth(), -size);
        return new ConfettiManager(context, this, source, container)
                .setVelocityX(0, velocitySlow)
                .setVelocityY(velocityNormal, velocitySlow)
                .setRotationalVelocity(180, 90)
                .setTouchEnabled(true);
    }

    public class FlowersViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public FlowersViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.flower);
            textView = itemView.findViewById(R.id.flowerName);
        }
    }
}
