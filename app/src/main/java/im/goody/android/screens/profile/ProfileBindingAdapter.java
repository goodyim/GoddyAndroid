package im.goody.android.screens.profile;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;

public class ProfileBindingAdapter {
    @BindingAdapter("profile_avatar")
    public static void bindAvatar(ImageView view, String url) {
        Picasso.get()
                .load(url)
                .placeholder(R.drawable.round_drawable)
                .fit()
                .into(view);
    }
}