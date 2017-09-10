package im.goody.android.screens.new_event;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNewEventBinding;

public class NewEventView extends BaseView<NewEventController, ScreenNewEventBinding> {
    public NewEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.newEventDate.setOnClickListener(v -> controller.chooseDate());
        binding.newEventLocation.setOnClickListener(v -> controller.chooseLocation());
        binding.newEventImageChooser.setOnClickListener(v -> controller.choosePhoto());
        binding.newEventPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newEventClearImage.setOnClickListener(v -> controller.clearPhoto());
    }

    @Override
    protected void onDetached() {

    }

    public void setData(NewEventViewModel viewModel) {
        binding.setViewModel(viewModel);
    }
}
