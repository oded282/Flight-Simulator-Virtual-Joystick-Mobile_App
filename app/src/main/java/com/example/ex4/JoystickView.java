package com.example.ex4;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/*
 This window responsible of the joystick who controls the simulator.
 Using the joystick the user can control the plane.
 The joystick has few parameters: center, radius of the base circle, radius of the
 small circle and joystick callback.

 */
public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private double centerX;
    private double centerY;
    private double baseRadius;
    private double hatRaduis;
    private JoystickListener joystickCallback;
    private final int ratio = 5; //The smaller, the more shading will occur

    /*
     This function initialize the joystick members.
     */
    void setupDimensions() {
        centerX = getWidth() / 2.0;
        centerY = getHeight() / 2.0;
        baseRadius = Math.min(getWidth(), getHeight()) / 3.0;
        hatRaduis = Math.min(getWidth(), getHeight()) / 7.0;
    }

    /*
     This func draws the joystick on top of the screen.
     */
    private void drawJoystick(double newX, double newY) {
        if (getHolder().getSurface().isValid()) {
            Canvas myCanvas = this.getHolder().lockCanvas(); //Stuff to draw
            Paint colors = new Paint();
            myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); // Clear the BG

            //First determine the sin and cos of the angle that the touched point is at relative to the center of the joystick
            float hypotenuse = (float) Math.sqrt(Math.pow(newX - centerX, 2) + Math.pow(newY - centerY, 2));
            float sin = (float) ((newY - centerY) / hypotenuse); //sin = o/h
            float cos = (float) (newX - centerX) / hypotenuse; //cos = a/h

            //Draw the base first before shading
            colors.setARGB(255, 100, 100, 100);
            myCanvas.drawCircle((float) centerX, (float) centerY, (float) baseRadius, colors);
            for (int i = 1; i <= (int) (baseRadius / ratio); i++) {
                colors.setARGB(150 / i, 255, 0, 0); //Gradually decrease the shade of black drawn to create a nice shading effect
                myCanvas.drawCircle((float) (newX - cos * hypotenuse * (ratio / baseRadius) * i),
                        (float) (newY - sin * hypotenuse * (ratio / baseRadius) * i), (float) (i * (hatRaduis * ratio / baseRadius)), colors); //Gradually increase the size of the shading effect
            }

            //Drawing the joystick hat
            for (int i = 0; i <= (int) (hatRaduis / ratio); i++) {
                colors.setARGB(255, (int) (i * (255 * ratio / hatRaduis)), (int) (i * (255 * ratio / hatRaduis)), 255); //Change the joystick color for shading purposes
                myCanvas.drawCircle((float) newX, (float) newY, (float) hatRaduis - (float) i * (ratio) / 2, colors); //Draw the shading for the hat
            }

            getHolder().unlockCanvasAndPost(myCanvas); //Write the new drawing to the SurfaceView
        }
    }

    /*
     This is the joystick constructor.
     */
    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /*
     This is the joystick constructor.
     */
    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /*
     This is the joystick constructor.
    */
    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof JoystickListener) {
            joystickCallback = (JoystickListener) context;
        }
    }

    /*
     This func creates the joystick surface, and draw it on the screen.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setupDimensions();
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /*
        This func responsible of "listening" to screen motion,
        detect and move the joystick as follows.
    */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this)) {
            if (motionEvent.getAction() != motionEvent.ACTION_UP) {
                float displacement = (float) Math.sqrt((Math.pow(motionEvent.getX() - centerX, 2)) +
                        Math.pow(centerY - motionEvent.getY(), 2));
                if (displacement < baseRadius) {
                    drawJoystick(motionEvent.getX(), motionEvent.getY());
                    joystickCallback.onJoystickMoved((float)
                                    ((motionEvent.getX() - centerX) / baseRadius),
                            (float) ((centerY - motionEvent.getY()) / baseRadius), getId());
                } else {
                    double ratio = baseRadius / displacement;
                    double constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
                    double constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    joystickCallback.onJoystickMoved((float) ((constrainedX - centerX) / baseRadius),
                            (float) ((centerY - constrainedY) / baseRadius), getId());
                }
            } else {
                drawJoystick(centerX, centerY);
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
        }
        return true;
    }

    /*
     This interface got method that "listen" to the screen motion.
     */
    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent, int id);
    }
}
