package im.goody.android.screens.common;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.utils.BitmapUtils;
import im.goody.android.utils.NetUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings("unused")
public class PostBindingAdapter {
    @BindingAdapter("post_image")
    public static void bindImage(ImageView view, Deal deal) {
        if (deal.getImageUrl() == null) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        Picasso.with(view.getContext())
                .load(NetUtils.buildDealImageUrl(deal))
                .placeholder(R.color.placeholder_color)
                .into(view);
    }

    @BindingAdapter("author_avatar")
    public static void bindAvatar(ImageView view, String url) {

        view.setImageResource(R.drawable.round_drawable);

        Observable.just(url)
                .subscribeOn(Schedulers.io())
                .map(s -> Picasso.with(view.getContext())
                        .load(url)
                        .get())
                .map(bitmap -> BitmapUtils.prepareAvatar(bitmap, view.getContext(), view.getWidth()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        view::setImageDrawable,
                        error -> view.setImageResource(R.drawable.round_drawable)
                );
    }
}
