package im.goody.android.screens.new_event;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNewEventBinding;

public class NewEventView extends BaseView<NewEventController, ScreenNewEventBinding> {
    public NewEventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.newEventDate.setOnClickListener(v -> controller.chooseDate());
        binding.newEventLocation.setOnClickListener(v -> controller.showLocationDialog());
        binding.newEventImageChooser.setOnClickListener(v -> controller.choosePhoto());
        binding.newEventPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newEventClearImage.setOnClickListener(v -> controller.clearPhoto());
        binding.addTag.setOnClickListener(v -> controller.addTag());
    }

    @Override
    protected void onDetached() {

    }

    public void setData(NewEventViewModel viewModel) {
        binding.setViewModel(viewModel);
        setTags(viewModel.tags);
    }

    public void setTags(List<String> tags) {
        binding.tagContainer.removeAllViews();
        for(String tag : tags) {
            addTag(tag);
        }
    }

    public void addTag(String tag) {
        ViewGroup container = binding.tagContainer;

        LayoutInflater inflater = LayoutInflater.from(getContext());

        TextView view = (TextView) inflater.inflate(R.layout.deletable_tag, container, false);
        view.setText(tag);
        view.setOnClickListener(v -> controller.removeTag(tag));

        container.addView(view);
    }

    public void removeTag(int position) {
        binding.tagContainer.removeViewAt(position);
    }
}
