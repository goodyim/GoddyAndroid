package im.goody.android.screens.common;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Event;
import im.goody.android.data.dto.Event.PhoneInfo;
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
        Picasso.with(view.getContext())
                .cancelRequest(view);


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
        Picasso.with(view.getContext())
                .cancelRequest(view);

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

    @BindingAdapter("participants")
    public static void bindParticipants(LinearLayout container, List<String> avatars) {
        container.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(container.getContext());

        if (avatars == null) return;
        for (int i = 0; i < avatars.size(); i++) {
            ImageView view = (ImageView) inflater.inflate(R.layout.participant_avatar, container, false);
            container.addView(view);

            if (i != 0) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
                params.setMargins(-(int) view.getResources().getDimension(R.dimen.size_small), 0, 0, 0);
            }

            Picasso.with(view.getContext())
                    .load(avatars.get(i))
                    .placeholder(R.drawable.round_drawable)
                    .fit()
                    .centerCrop()
                    .into(view);
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

    @BindingAdapter("link_text")
    public static void bindLinkedText(AutoLinkTextView view, String text) {
        view.setLinkedText(text);
    }

    @BindingAdapter({"join_participates", "join_status"})
    public static void bindJoinStyle(TextView view, boolean participates, String status) {
        int background = 0, title = 0;

        switch (status) {
            case Event.ACTIVE:
                background = R.drawable.join_active;
                title = participates ? R.string.leave : R.string.active_join;
                view.setEnabled(true);
                break;
            case Event.IN_PROGRESS:
                background = R.drawable.join_process;
                title = participates ? R.string.leave : R.string.process_join;
                view.setEnabled(true);
                break;
            case Event.CLOSED:
                background = R.drawable.join_finished;
                title = R.string.finished_event;
                view.setEnabled(false);
        }

        view.setBackgroundResource(background);
        view.setText(title);
    }

    @BindingAdapter("tags")
    public static void bindTags(FlexboxLayout container, String resources) {
        container.removeAllViews();

        if (TextUtils.isEmpty(resources)) return;

        String[] tags = resources.split(",");

        for (String tag : tags) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            TextView view = (TextView) inflater.inflate(R.layout.event_tag, container, false);
            view.setText(tag);
            container.addView(view);
        }
    }

    @BindingAdapter(value = {"date", "time_disabled", "date_immediately", "date_hint"}, requireAll = false)
    public static void bindDate(TextView view, Calendar calendar, Boolean timeDisabled, boolean immediately, String hint) {
        String date;

        if (immediately) {
            date = App.getAppContext().getString(R.string.date_immediately);
        } else if (calendar == null) {
            date = hint != null ? hint : view.getContext().getString(R.string.choose_date);
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

    @BindingAdapter("request_state")
    public static void bindRequest(Button button, int state) {
        int buttonVisibility = View.VISIBLE;
        boolean buttonEnabled = true;
        int stringRes = R.string.forbidden;

        switch (state) {
            case PhoneInfo.STATE_ALLOWED:
                buttonVisibility = View.GONE;
                break;
            case PhoneInfo.STATE_REQUEST_NEEDED:
                buttonVisibility = View.VISIBLE;
                stringRes = R.string.make_request;
                break;
            case PhoneInfo.STATE_REQUESTED:
                buttonVisibility = View.VISIBLE;
                buttonEnabled = false;
                stringRes = R.string.requested;
                break;
            case PhoneInfo.STATE_FORBIDDEN:
                buttonVisibility = View.VISIBLE;
                buttonEnabled = false;
                stringRes = R.string.forbidden;
        }

        button.setVisibility(buttonVisibility);
        button.setEnabled(buttonEnabled);
        button.setText(stringRes);
    }
}
