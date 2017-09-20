package im.goody.android.screens.profile;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;

public class ProfileBindingAdapter {
    @BindingAdapter("profile_avatar")
    public static void bindAvatar(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.round_drawable)
                .fit()
                .into(view);
    }
}
