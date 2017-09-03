package im.goody.android.screens.main;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import im.goody.android.App;
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
    public static void bindDescription(TextView view, MainItemViewModel model, boolean isExpanded) {
        String original = model.getDeal().getDescription();
        Spannable result;
        if (isExpanded || original.length() <= COLLAPSED_CHAHACTERS_COUNT) {
            result = new SpannableString(original);
        } else {
            String part = original.substring(0, COLLAPSED_CHAHACTERS_COUNT).trim();
            result = addMoreLink(part, model);
        }

        view.setMovementMethod(LinkMovementMethod.getInstance());
        view.setText(result);
    }

    private static Spannable addMoreLink(String text, MainItemViewModel model) {
        int color = ContextCompat.getColor(App.getAppContext(), R.color.secondary_text);
        String more = App.getAppContext().getString(R.string.expand);

        SpannableString string = new SpannableString(text + " ..." + more);

        ClickableSpan click = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                model.setExpanded(true);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };

        string.setSpan(new ForegroundColorSpan(color), text.length(),
                string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        string.setSpan(click, text.length(), string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return string;
    }
}
