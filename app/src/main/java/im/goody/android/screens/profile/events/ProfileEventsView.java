package im.goody.android.screens.profile.events;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import java.util.List;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.PageProfileEventsBinding;

public class ProfileEventsView extends BaseView<ProfileEventsController, PageProfileEventsBinding> {

    public static final int SPAN_COUNT = 2;

    public ProfileEventsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.profileEventsList.setHasFixedSize(true);
        binding.profileEventsList.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    @Override
    protected void onDetached() {
        binding.profileEventsList.setAdapter(null);
    }

    public void showEvents(List<ProfileEventItemViewModel> events) {
        binding.profileEventsList.setAdapter(new ProfileEventsAdapter(events, controller));
    }
}
