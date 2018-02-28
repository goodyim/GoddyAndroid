package im.goody.android.screens.profile;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import im.goody.android.App;
import im.goody.android.R;

public class ProfileBindingAdapter {
    @BindingAdapter("profile_avatar")
    public static void bindAvatar(ImageView view, String url) {
        App.picasso.load(url)
                .placeholder(R.drawable.round_drawable)
                .fit()
                .into(view);
    }
}