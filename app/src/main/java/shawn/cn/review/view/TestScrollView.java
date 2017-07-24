package shawn.cn.review.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * Created by Shawn on 2017/7/11.
 */

public class TestScrollView extends View {
    public TestScrollView(Context context) {
        super(context);
    }

    public TestScrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestScrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }



    private Scroller mScroller = new Scroller(getContext());

    private void smoothScrollTo(int destX,int destY){
        int scrollX = getScrollX();
        int scrollY = getScrollY();
        int deltaX = destX - scrollX;
        int deltaY = destY -scrollY;
        mScroller.startScroll(scrollX,scrollY,deltaX,deltaY,1000);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }
}
