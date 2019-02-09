package im.goody.android.screens.feedback;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import im.goody.android.Constants.NotificationExtra;
import im.goody.android.R;

public class FeedbackBindingAdapter {

    @BindingAdapter("feedback_body")
    public static void bindFeedbackBody(TextView view, String type) {
        int stringRes;

        switch (type) {
            case NotificationExtra.TYPE_COMMENT:
                stringRes = R.string.feedback_comment;
                break;
            case NotificationExtra.TYPE_MENTION:
                stringRes = R.string.feedback_mentioned;
                break;
            case NotificationExtra.TYPE_CLOSE_EVENT:
                stringRes = R.string.feedback_close_event;
                break;
            case NotificationExtra.TYPE_PHONE_REQUEST:
                stringRes = R.string.feedback_phone_request;
                break;
            default:
                stringRes = R.string.feedback_common;
        }

        view.setText(stringRes);
    }
}