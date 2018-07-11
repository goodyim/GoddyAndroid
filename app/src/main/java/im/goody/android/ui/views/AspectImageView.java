package im.goody.android.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import im.goody.android.R;

public class AspectImageView extends AppCompatImageView {
    private static final float DEFAULT_ASPECT_RATIO = 1.77f;
    private static final boolean HEIGHT_MAIN_DEFAULT = false;
    private final float aspectRatio;
    private final boolean isHeightMain;

    public AspectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AspectImageView);
        aspectRatio = a.getFloat(R.styleable.AspectImageView_aspect_ratio, DEFAULT_ASPECT_RATIO);
        isHeightMain = a.getBoolean(R.styleable.AspectImageView_is_height_main, HEIGHT_MAIN_DEFAULT);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int newWidth;
        int newHeight;

        if (isHeightMain) {
            newHeight = getMeasuredHeight();
            newWidth = (int) (newHeight / aspectRatio);
        } else {
            newWidth = getMeasuredWidth();
            newHeight = (int) (newWidth / aspectRatio);
        }

        setMeasuredDimension(newWidth, newHeight);
    }
}
