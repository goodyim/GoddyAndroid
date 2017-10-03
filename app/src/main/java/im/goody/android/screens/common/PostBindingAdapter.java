package im.goody.android.screens.common;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;

@SuppressWarnings("unused")
public class PostBindingAdapter {
    @BindingAdapter("post_image")
    public static void bindImage(ImageView view, Deal deal) {
        Picasso.with(view.getContext()).cancelRequest(view);

        if (deal.getImageUrl() == null) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);
        Picasso.with(view.getContext())
                .load(NetUtils.buildDealImageUrl(deal))
                .placeholder(R.color.placeholder_color)
                .fit()
                .centerCrop()
                .into(view);
    }

    @BindingAdapter("author_avatar")
    public static void bindAvatar(ImageView view, String url) {
        Picasso.with(view.getContext()).cancelRequest(view);

        view.setImageResource(R.drawable.round_drawable);

        Picasso.with(view.getContext())
                .load(url)
                .placeholder(R.drawable.round_drawable)
                .fit()
                .into(view);
    }

    @BindingAdapter("event_date")
    public static void bindDate(TextView view, String date) {
        if (date != null) {
            view.setText(DateUtils.getAbsoluteDate(date));
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("address")
    public static void bindLocation(TextView view, Location location) {
        if (location != null && !TextUtils.isEmpty(location.getAddress())) {
            view.setText(location.getAddress());
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @BindingAdapter({"likes", "liked"})
    public static void bindLikes(ViewGroup view, int likes, boolean liked) {
        ImageView imageView = (ImageView) view.getChildAt(0);
        TextView textView = (TextView) view.getChildAt(1);

        textView.setText(TextUtils.formatCount(likes));

        int colorRes = liked ? R.color.accent : R.color.icon;
        int color = ContextCompat.getColor(view.getContext(), colorRes);

        textView.setTextColor(color);
        imageView.setColorFilter(color);
    }


    @BindingAdapter("title")
    public static void bindTitle(TextView view, String title) {
        String result;
        if (TextUtils.isEmpty(title)) {
            result = view.getContext().getString(R.string.event_default_title);
        } else {
            result = title;
        }
        view.setText(result);
    }
}
