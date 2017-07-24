package cn.shawn.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.ScrollView;

/**
 * Created by Shawn on 2017/7/13.
 */

public class NsListView extends ListView {

    public NsListView(Context context) {
        super(context);
    }

    public NsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, spec);
    }

}
