package ru.alexangan.developer.geatech.ViewOverrides;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import ru.alexangan.developer.geatech.Activities.MainActivity;

/**
 * Created by user on 31.05.2017.
 */

public class ScrollViewEx extends ScrollView
{
    private MainActivity scrollViewListener = null;
    Context context;

    public ScrollViewEx(Context context)
    {
        super(context);
        this.context = context;
    }

    public ScrollViewEx(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScrollViewEx(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY)
    {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (scrollViewListener != null)
        {
            scrollViewListener.onScrollChanged(ScrollViewEx.this, scrollX, scrollY, 0, 0);
        }
    }

    public void setScrollViewListener(MainActivity scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                Log.i("VerticalScrollview", "onInterceptTouchEvent: DOWN super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_MOVE:
                return false; // redirect MotionEvents to ourself

            case MotionEvent.ACTION_CANCEL:
                Log.i("VerticalScrollview", "onInterceptTouchEvent: CANCEL super false" );
                super.onTouchEvent(ev);
                break;

            case MotionEvent.ACTION_UP:
                Log.i("VerticalScrollview", "onInterceptTouchEvent: UP super false" );
                return false;

            default: Log.i("VerticalScrollview", "onInterceptTouchEvent: " + action ); break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        Log.i("VerticalScrollview", "onTouchEvent. action: " + ev.getAction() );
        return true;
    }*/
}
