package im.goody.android.screens.setting;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenSettingBinding;

public class SettingView extends BaseView<SettingController, ScreenSettingBinding> {

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.settingSubmit.setOnClickListener(v -> controller.saveSettings());
        binding.settingChooseHelp.setOnClickListener(v -> controller.chooseHelp());
    }

    @Override
    protected void onDetached() {
        binding.settingSubmit.setOnClickListener(null);
        binding.settingChooseHelp.setOnClickListener(null);
    }

    void setViewModel(SettingViewModel viewModel) {
        binding.setModel(viewModel);
    }
}
