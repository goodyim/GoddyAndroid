package im.goody.android.screens.intro.resources;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import im.goody.android.R;
import im.goody.android.core.BaseView;
import im.goody.android.databinding.IntroResourcesBinding;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;

public class ResourcesView extends BaseView<ResourcesController, IntroResourcesBinding> {
    public ResourcesView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.addTag.setOnClickListener(v -> controller.addTag());
    }

    @Override
    protected void onDetached() {}

    public void setData(ChooseHelpViewModel viewModel) {
        binding.tagContainer.removeAllViews();
        for(String tag : viewModel.tags) {
            addTag(tag);
        }

        binding.presetTagContainer.removeAllViews();
        for(ChooseHelpViewModel.PresetTag tag: viewModel.presetTags) {
            addPreset(tag);
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

    public void addPreset(ChooseHelpViewModel.PresetTag tag) {
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
}
