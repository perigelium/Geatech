package ru.alexangan.developer.geatech.Interfaces;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import ru.alexangan.developer.geatech.Activities.MainActivity;

/**
 * Created by user on 31.05.2017.
 */

public class ScrollViewExt extends ScrollView
{
    private MainActivity scrollViewListener = null;
    Context context;

    public ScrollViewExt(Context context)
    {
        super(context);
        this.context = context;
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY)
    {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);

        if (scrollViewListener != null)
        {
            scrollViewListener.onScrollChanged(ScrollViewExt.this, scrollX, scrollY, 0, 0);
        }
    }

    public ScrollViewExt(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void setScrollViewListener(MainActivity scrollViewListener)
    {
        this.scrollViewListener = scrollViewListener;
    }
}
