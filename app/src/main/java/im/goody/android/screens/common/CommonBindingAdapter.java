package im.goody.android.screens.common;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Deal.Event;
import im.goody.android.data.dto.Location;
import im.goody.android.ui.views.AutoLinkTextView;
import im.goody.android.utils.DateUtils;
import im.goody.android.utils.NetUtils;
import im.goody.android.utils.TextUtils;

import static im.goody.android.Constants.DATE_FORMAT;
import static im.goody.android.Constants.DATE_TIME_FORMAT;

@SuppressWarnings("unused")
public class CommonBindingAdapter {
    @BindingAdapter("post_image")
    public static void bindImage(ImageView view, Deal deal) {
        App.picasso.cancelRequest(view);

        if (deal.getImageUrl() == null) {
            view.setVisibility(View.GONE);
            return;
        }
        view.setVisibility(View.VISIBLE);

        App.picasso.load(NetUtils.buildDealImageUrl(deal))
                .placeholder(R.color.placeholder_color)
                .fit()
                .centerCrop()
                .into(view);
    }

    @BindingAdapter("author_avatar")
    public static void bindAvatar(ImageView view, String url) {
        App.picasso.cancelRequest(view);

        view.setImageResource(R.drawable.round_drawable);

        App.picasso.load(url)
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

    @BindingAdapter("event_resources")
    public static void bindResources(TextView view, String resources) {
        if (resources != null) {
            view.setText(resources);
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

    @BindingAdapter("link_text")
    public static void bindLinkedText(AutoLinkTextView view, String text) {
        view.setLinkedText(text);
    }

    @BindingAdapter("event_color")
    public static void bindEventColor(View view, String state) {
        int backgroundRes = 0;

        switch (state) {
            case Event.ACTIVE:
                backgroundRes = R.drawable.event_header_active;
                break;
            case Event.IN_PROGRESS:
                backgroundRes = R.drawable.event_header_in_progress;
                break;
            case Event.CLOSED:
                backgroundRes = R.drawable.event_header_closed;
        }

        view.setBackgroundResource(backgroundRes);
    }

    @BindingAdapter(("join_visibility"))
    public static void bindJoinVisibility(View view, String state) {
        int visibility = View.VISIBLE;

        if (state.equals(Event.CLOSED))
            visibility = View.INVISIBLE;

        view.setVisibility(visibility);
    }

    @BindingAdapter(value = {"date", "time_disabled"}, requireAll = false)
    public static void bindDate(TextView view, Calendar calendar, Boolean timeDisabled) {
        String date;

        if (calendar == null) {
            date = view.getContext().getString(R.string.choose_date);
        } else {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            if (timeDisabled == null || !timeDisabled)
                date = String.format(Locale.getDefault(), DATE_TIME_FORMAT,
                    day, month, year, hours, minutes);
            else
                date = String.format(Locale.getDefault(), DATE_FORMAT, day, month, year);
        }

        view.setText(date);
    }
}
