package im.goody.android.screens.main;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;
import im.goody.android.utils.BitmapUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class MainPostBindingAdapter {
    @BindingAdapter("item_image")
    public static void bindImage(ImageView view, String url) {
        if (url == null) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.color.placeholder_color)
                .into(view);
    }

    @BindingAdapter("user_avatar")
    public static void bindAvatar(ImageView view, String url) {
        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(s -> Picasso.with(view.getContext())
                        .load(s)
                        .get())
                .map(bitmap -> BitmapUtils.prepareAvatar(bitmap, view.getContext(), view.getWidth()))
                .subscribe(view::setImageDrawable,
                        error -> view.setImageResource(R.drawable.round_drawable)
                );
    }

}
