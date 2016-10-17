package com.docgenerici.selfbox.android.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.docgenerici.selfbox.R;


/**
 * @author Giuseppe Sorce  @copyright
 */

public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
    private  int marginLeftRight=0;
    private Drawable mDivider;

    public SimpleDividerItemDecoration(Context context) {
        mDivider = context.getResources().getDrawable(R.drawable.line_divider);
    }
    public SimpleDividerItemDecoration(Context context, Drawable line) {
        mDivider = line;
    }
    public SimpleDividerItemDecoration(Context context, Drawable line, int margin) {
        mDivider = line;
        marginLeftRight= margin;
    }
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft() + marginLeftRight;
        int right = parent.getWidth() - parent.getPaddingRight() - marginLeftRight ;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }
}
