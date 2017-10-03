package im.goody.android.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import im.goody.android.R;

public class AspectImageView extends AppCompatImageView {
    private static final float DEFAULT_ASPECT_RATIO = 1.73f;
    private final float mAspectRatio;

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectImageView);
        mAspectRatio = a.getFloat(R.styleable.AspectImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth;
        int newHeight;
        newWidth  = getMeasuredWidth();
        newHeight = (int) (newWidth / mAspectRatio);
        setMeasuredDimension(newWidth, newHeight);
    }
}
