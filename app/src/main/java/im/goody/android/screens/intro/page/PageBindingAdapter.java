package im.goody.android.screens.intro.page;

import android.databinding.BindingAdapter;
import android.support.v7.widget.AppCompatImageView;

public class PageBindingAdapter {

    @BindingAdapter("icon")
    public static void bindIcon(AppCompatImageView imageView, int iconId) {
        imageView.setBackgroundResource(iconId);
    }
}
