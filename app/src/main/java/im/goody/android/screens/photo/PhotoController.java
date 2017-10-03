package im.goody.android.screens.photo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import dagger.Subcomponent;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;

public class PhotoController extends BaseController<PhotoView> {
    private static final String URL_KEY = "PhotoController.url";

    private PhotoViewModel viewModel = new PhotoViewModel();

    public PhotoController(String url) {
        super(new BundleBuilder()
                .putString(URL_KEY, url)
                .build());
    }

    public PhotoController(Bundle args) {
        super(args);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusPhoto();
        if (component != null) component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setTitleRes(R.string.photo_title)
                .setToolbarVisible(false)
                .setStatusBarVisible(false)
                .setHomeState(BarBuilder.HOME_GONE)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_photo;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setUrl(getUrl());
    }

    private String getUrl() {
        return getArgs().getString(URL_KEY);
    }

    void finish() {
        getActivity().onBackPressed();
    }

    @Subcomponent
    @DaggerScope(PhotoController.class)
    public interface Component {
        void inject(PhotoController controller);
    }
}
