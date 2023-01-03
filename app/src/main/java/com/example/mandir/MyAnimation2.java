package com.example.mandir;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class MyAnimation2 extends Animation {
    private View view;
    private float cx, cy;           // center x,y position of circular path
    private float prevX, prevY;     // previous x,y position of image during animation
    private float r;                // radius of circle
    private float prevDx, prevDy;

    public MyAnimation2(View view, float r){
        this.view = view;
        prevY=view.getY();
        this.r = r;
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        // calculate position of image center
        int cxImage = width / 2;
        int cyImage = height / 2;
        cx = view.getLeft() + cxImage;
        cy = view.getTop() + cyImage;

        // set previous position to center
        prevX = cx;
        prevY = cy;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        if(interpolatedTime == 0){
            t.getMatrix().setRotate(0,prevDx,prevDy);
            return;
        }

        float angleDeg = (interpolatedTime * 180f) % 360;
        float angleRad = (float) Math.toRadians(angleDeg);

        // r = radius, cx and cy = center point, a = angle (radians)
        float x = (float) ( cx+r * Math.cos(angleRad));


        float dx = prevX - x;
        float dy=prevY;


        prevX = x;


        prevDx = dx;




        prevX = x;


        prevDx = dx;


        t.getMatrix().setRotate(angleDeg,dx,dy);
    }
}
