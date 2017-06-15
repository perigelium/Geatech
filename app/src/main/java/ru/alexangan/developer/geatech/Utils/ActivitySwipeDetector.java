package ru.alexangan.developer.geatech.Utils;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by user on 15.06.2017.
 */

public class ActivitySwipeDetector implements View.OnTouchListener
{

    private Activity activity;
    static final int MIN_DISTANCE = 0;
    private float downX, downY, upX, upY;

    public ActivitySwipeDetector(final Activity activity)
    {
        this.activity = activity;
    }

    public void onRightToLeftSwipe()
    {
        Log.i("DEBUG", "RightToLeftSwipe!");
    }

    public void onLeftToRightSwipe()
    {
        Log.i("DEBUG", "LeftToRightSwipe!");
    }

    public void onTopToBottomSwipe()
    {
        Log.i("DEBUG", "onTopToBottomSwipe!");
    }

    public void onBottomToTopSwipe()
    {
        Log.i("DEBUG", "onBottomToTopSwipe!");
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                downX = event.getX();
                downY = event.getY();
                //   return true;
            }
            case MotionEvent.ACTION_UP:
            {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    // left or right
                    if (deltaX < 0)
                    {
                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if (deltaX > 0)
                    {
                        this.onRightToLeftSwipe();
                        return true;
                    }
                } else
                {
                    //Log.i("DEBUG", "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE)
                {
                    // top or down
                    if (deltaY < 0)
                    {
                        this.onTopToBottomSwipe();
                        return true;
                    }
                    if (deltaY > 0)
                    {
                        this.onBottomToTopSwipe();
                        return true;
                    }
                } else
                {
                    //Log.i("DEBUG", "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
                }

                //     return true;
            }
        }
        return false;
    }

/*    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        return false;
    }*/
}