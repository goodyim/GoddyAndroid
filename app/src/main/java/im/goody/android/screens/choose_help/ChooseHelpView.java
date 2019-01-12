package im.goody.android.screens.choose_help;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.List;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.data.dto.PresetTag;
import im.goody.android.databinding.ScreenChooseHelpBinding;

public class ChooseHelpView extends BaseView<ChooseHelpController, ScreenChooseHelpBinding> {
    public ChooseHelpView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onAttached() {
        binding.addTag.setOnClickListener(v -> controller.addTag());
        binding.chooseHelpSubmit.setOnClickListener(v -> controller.submit());
        binding.chooseHelpLocation.setOnClickListener(v -> controller.chooseLocation());
        binding.chooseHelpSkip.setOnClickListener(v -> controller.skip());
    }

    @Override
    protected void onDetached() {

    }

    public void setData(ChooseHelpViewModel viewModel) {
        binding.setViewModel(viewModel);

        setTags(viewModel.tags);

        binding.presetTagContainer.removeAllViews();
        for(PresetTag tag: viewModel.presetTags) {
            addPreset(tag);
        }
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

    private void addPreset(PresetTag tag) {
        ViewGroup container = binding.presetTagContainer;

        LayoutInflater inflater = LayoutInflater.from(getContext());

        CheckedTextView view = (CheckedTextView) inflater.inflate(R.layout.checkable_tag, container, false);
        view.setText(tag.getValue());
        view.setChecked(tag.isChecked());

        view.setOnClickListener(v -> {
            view.toggle();
            tag.setChecked(view.isChecked());
        });

        container.addView(view);
    }

    public void removeTag(int position) {
        binding.tagContainer.removeViewAt(position);
    }

    public void hideSkip() {
        binding.chooseHelpSkip.setVisibility(GONE);
    }

    public void showLocationFindingText() {
        binding.chooseHelpLocation.setText(R.string.finding_location);
    }
}
