package im.goody.android.screens.main;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenMainBinding;

import static im.goody.android.Constants.DEFAULT_ANIMATION_DURATION;

public class MainView extends BaseView<MainController, ScreenMainBinding> {

    private boolean isMenuOpened = false;

    public MainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttached() {
        binding.mainCreateFab.setOnClickListener(v -> {
            rotateMenuFab();
            if (isMenuOpened) {
                binding.mainNewEvent.hide();
                binding.mainNewPost.hide();
            } else {
                binding.mainNewPost.show();
                binding.mainNewEvent.show();
            }
            isMenuOpened = !isMenuOpened;
        });

        binding.mainNewPost.setOnClickListener(v -> controller.showNewPostScreen());
        binding.mainNewEvent.setOnClickListener(v -> controller.showNewEventScreen());
    }

    @Override
    protected void onDetached() {

    }

    public ViewPager getPager() {
        return binding.mainPager;
    }

    void setupPager() {
        binding.mainPager.setOffscreenPageLimit(3);
        binding.mainPager.setAdapter(new MainPageAdapter(controller));
    }

    private void rotateMenuFab() {
        FloatingActionButton fab = binding.mainCreateFab;
        float degrees = 45 * (isMenuOpened ? 0 : 1);
        fab.animate()
                .rotation(degrees)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .start();
    }
}
