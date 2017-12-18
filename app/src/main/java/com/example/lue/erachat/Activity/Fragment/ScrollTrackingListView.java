package com.example.lue.erachat.Activity.Fragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

public class ScrollTrackingListView extends ListView {

    private boolean readyForMeasurement = false;
    private Boolean isScrollable = null;
    private float prevDistanceToEnd = (float) -1.0;
    private ScrollDirectionListener listener = null;

    public ScrollTrackingListView(Context context) {
        super(context);
        init();
    }

    public ScrollTrackingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScrollTrackingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnGlobalLayoutListener(globalLayoutListener);
        setOnScrollListener(scrollListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener
            = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            readyForMeasurement = true;
            calculateDistanceToEnd();
        }

    };

   /* public void registerScrollDirectionListener(ScrollDirectionListener listener) {
        scrollDirectionListener = listener;
    }
*/
    /*public void unregisterScrollDirectionListener() {
        scrollDirectionListener = null;
    }*/

    private AbsListView.OnScrollListener scrollListener
            = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView absListView, int i) {
            calculateDistanceToEnd();
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            // Do nothing
        }

    };

    private void calculateDistanceToEnd() {

        if (readyForMeasurement) {

            // I'm using the height of the layout, horizontal scrollbar and
            // content along with scroll down offset

            // computeVerticalScrollExtent is used to compute the length of the thumb within the scrollbar's track.
            // The length of the thumb is a function of the view height and the content length.
            int verticalScrollExtent = computeVerticalScrollExtent();
            int verticalScrollOffset = computeVerticalScrollOffset();
            int verticalScrollRange = computeVerticalScrollRange();
            int horizontalScrollBarHeight = getHorizontalScrollbarHeight();

            /**
             * 1. Let "R" represent the range of the vertical scrollbar. This corresponds to the length of the content
             * in the view.
             * 2. Let "E" represent the extent of the vertical scrollbar. The extent is a constant value and is
             * (probably) equal to a value proportional to the height of the view.
             * 3. Offset "o" represents the current position in the range that is visible to the user. It can take
             * values from "0 to E".
             *
             * Now the DistanceToEnd is calculated using these three values as follows :
             *
             * DistanceToEnd = (R - o) / E
             *
             * DistanceToEnd will hold the value in NumberOfScreenToEnd units.
             *
             */

            float distanceToEnd =
                    ((float)(verticalScrollRange - verticalScrollOffset))/((float)(verticalScrollExtent));

            if(prevDistanceToEnd == -1) {
                prevDistanceToEnd = distanceToEnd;
            } else {
                if(listener != null) {
                    if(distanceToEnd > prevDistanceToEnd) {
                        // User is scrolling up
                        listener.onScrollingUp();
                    } else {
                        // User is scrolling up
                        listener.onScrollingDown();
                    }
                }
                prevDistanceToEnd = distanceToEnd;
            }

            if(isScrollable == null) {
                // Check if the view height is less than a screen (i.e., no scrolling is enabled)
                if((horizontalScrollBarHeight + verticalScrollExtent) >= verticalScrollRange) {
                    isScrollable = Boolean.FALSE;
                } else {
                    isScrollable = Boolean.TRUE;
                }
            }

        }

    }

    public interface ScrollDirectionListener {

        public void onScrollingUp();

        public void onScrollingDown();

    }

}