package im.goody.android.screens.new_post;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;

public class NewPostBindingAdapter {

    @BindingAdapter("location")
    public static void bindLocation(TextView textView, Place place) {
        if (place == null) {
            textView.setText(null);
            ((View) textView.getParent()).setVisibility(View.GONE);
        } else {
            textView.setText(place.getAddress());
            ((View) textView.getParent()).setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("photo")
    public static void bindPhoto(ImageView imageView, Bitmap bitmap) {
        if (bitmap == null) {
            imageView.setVisibility(View.GONE);
        } else {
            imageView.setImageBitmap(bitmap);
            imageView.setVisibility(View.VISIBLE);
        }
    }
}
