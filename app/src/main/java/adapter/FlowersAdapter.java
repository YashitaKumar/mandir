package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    ImageView f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11;
    ConstraintLayout parent;

    public void setImageAssets(ImageView f1,ImageView f2,ImageView f3,ImageView f4,ImageView f5,ImageView f6,ImageView f7,ImageView f8,ImageView f9,ImageView f10,ImageView f11,ConstraintLayout parent){
        this.f1 = f1;
        this.f2 = f2;
        this.f3 = f3;
        this.f4 = f4;
        this.f5 = f5;
        this.f6 = f6;
        this.f7 = f7;
        this.f8 = f8;
        this.f9 = f9;
        this.f10 = f10;
        this.f11 = f11;
        this.parent =parent;

    }

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
                        .setEmissionRate(15)
                        .animate();
                //Flowers on plate
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
                f1.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f2.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f3.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f4.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f5.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f6.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f7.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f8.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f9.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f10.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
                f11.setImageDrawable(AppCompatResources.getDrawable(context,arrImages[pos]));
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
