package im.goody.android.screens.main;

import android.databinding.BindingAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import im.goody.android.App;
import im.goody.android.R;
import im.goody.android.ui.views.AutoLinkTextView;

import static im.goody.android.Constants.COLLAPSED_CHARACTERS_COUNT;

public class MainBindingAdapter {
    @BindingAdapter({"description", "expandState"})
    public static void bindDescription(AutoLinkTextView view, MainItemViewModel model, boolean isExpanded) {
        String original = model.getDeal().getDescription();
        Spannable result;
        if (isExpanded || original.length() <= COLLAPSED_CHARACTERS_COUNT) {
            result = new SpannableString(original);
        } else {
            String part = original.substring(0, COLLAPSED_CHARACTERS_COUNT).trim();
            result = addMoreLink(part, model);
        }

        view.setLinkedText(result);
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
