package im.goody.android.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.goody.android.R;

public class AutoLinkTextView extends AppCompatTextView {
    private MentionClickListener mentionListener;
    private Pattern mentionPattern = Pattern.compile("(?<![A-Za-z0-9_\\-])\\s*@[A-Za-z0-9_\\-]+", Pattern.CASE_INSENSITIVE);

    public AutoLinkTextView(Context context) {
        super(context);
    }



    public AutoLinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);


        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AutoLinkTextView);

        String text = array.getString(R.styleable.AutoLinkTextView_linked_text);

        setLinkedText(text);

        array.recycle();

        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setMentionListener(MentionClickListener mentionListener) {
        this.mentionListener = mentionListener;

    }

    public CharSequence getLinkedText() {
        return getText();
    }

    public void setLinkedText(Spannable spannable) {
        String text = spannable.toString();

        Matcher mentionMatcher = mentionPattern.matcher(text);

        while (mentionMatcher.find()) {
            String tag = mentionMatcher.group();

            NonUnderScoredSpan span = new NonUnderScoredSpan() {
                @Override
                public void onClick(View view) {
                    if (mentionListener != null)
                        mentionListener.onMentionClick(tag.split("@")[1]);
                }
            };

            spannable.setSpan(span, mentionMatcher.start(), mentionMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(spannable);

        setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setLinkedText(@Nullable String text) {
        if (text == null) {
            setText(null);
            return;
        }

        setLinkedText(new SpannableString(text));
    }

    public interface MentionClickListener {
        void onMentionClick(String mention);
    }

    private abstract class NonUnderScoredSpan extends ClickableSpan {
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }
    }
}
