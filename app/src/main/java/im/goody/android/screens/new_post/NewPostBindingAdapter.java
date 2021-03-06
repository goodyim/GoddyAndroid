package im.goody.android.screens.new_post;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import im.goody.android.R;
import im.goody.android.data.dto.Location;

public class NewPostBindingAdapter {

    @BindingAdapter("location")
    public static void bindLocation(TextView textView, Location location) {
        if (location == null) {
            textView.setText(null);
            setParentVisibility(textView, View.GONE);
        } else {
            textView.setText(location.getAddress());
            setParentVisibility(textView, View.VISIBLE);
        }
    }

    @BindingAdapter("photo")
    public static void bindPhoto(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            setParentVisibility(imageView, View.GONE);
        } else {
            imageView.setImageBitmap(bitmap);
            setParentVisibility(imageView, View.VISIBLE);
        }
    }

    @BindingAdapter("state_icon")
    public static void bindStateIcon(ImageView view, Object object) {
        int filter = object == null ? R.color.icon : R.color.accent;
        int color = ContextCompat.getColor(view.getContext(), filter);
        view.setColorFilter(color);
    }

    private static void setParentVisibility(View view, int visibility) {
        ((View) view.getParent()).setVisibility(visibility);
    }
}
