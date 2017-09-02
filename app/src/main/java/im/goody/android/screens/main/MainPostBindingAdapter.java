package im.goody.android.screens.main;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.utils.BitmapUtils;
import im.goody.android.utils.NetUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static im.goody.android.Constants.COLLAPSED_CHAHACTERS_COUNT;

public class MainPostBindingAdapter {
    @BindingAdapter("item_image")
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

    @BindingAdapter("user_avatar")
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
    @BindingAdapter({"description", "expandState"})
    public static void bindDescription(TextView view, String text, boolean isExpanded) {
        String result;
        if (isExpanded || text.length() <= COLLAPSED_CHAHACTERS_COUNT) {
            result = text;
        } else {
            result = text.substring(0, COLLAPSED_CHAHACTERS_COUNT).trim() + "...";
        }
        view.setText(result);
    }

    @BindingAdapter({"descriptionLength", "expandState"})
    public static void bindExpandState(TextView textView, String text, boolean isExpanded) {
        if (text.length() <= COLLAPSED_CHAHACTERS_COUNT) {
            textView.setVisibility(View.GONE);
            return;
        } else {
            textView.setVisibility(View.VISIBLE);
        }

        int labelRes = isExpanded ? R.string.collapse : R.string.expand;
        textView.setText(labelRes);
    }
}
