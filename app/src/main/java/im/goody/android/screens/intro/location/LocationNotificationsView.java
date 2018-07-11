package im.goody.android.screens.intro.location;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.IntroLocationBinding;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;

public class LocationNotificationsView extends BaseView<LocationNotificationsController, IntroLocationBinding> {
    public LocationNotificationsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.chooseHelpLocation.setOnClickListener(v -> controller.chooseLocation());
    }

    @Override
    protected void onDetached() {

    }

    public void setData(ChooseHelpViewModel viewModel) {
        binding.setViewModel(viewModel);
    }
}
