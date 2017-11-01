package im.goody.android.screens.profile;

import android.databinding.BindingAdapter;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;
import im.goody.android.utils.BitmapUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfileBindingAdapter {
    @BindingAdapter("profile_avatar")
    public static void bindAvatar(ImageView view, String url) {
        if (url != null)
            Observable.just(url)
                    .subscribeOn(Schedulers.io())
                    .map(s -> Picasso.with(view.getContext())
                            .load(url)
                            .placeholder(R.drawable.round_drawable)
                            .get())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(view::setImageBitmap)
                    .observeOn(Schedulers.io())
                    .map(bitmap -> BitmapUtils.blur(view.getContext(), bitmap))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        View parent = (View) view.getParent();

                        parent.setBackground(new BitmapDrawable(parent.getResources(), bitmap));
                    });
    }
}
