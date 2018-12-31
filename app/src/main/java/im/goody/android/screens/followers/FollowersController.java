package im.goody.android.screens.followers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.BundleBuilder;
import io.reactivex.disposables.Disposable;

public class FollowersController extends BaseController<FollowersView> {
    private FollowersViewModel viewModel = new FollowersViewModel();

    private static final String ID_KEY = "ID_KEY";

    public FollowersController(String postId) {
        super(new BundleBuilder()
                .putLong(ID_KEY, Long.valueOf(postId))
                .build());
    }

    public FollowersController(Bundle args) {
        super(args);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusFollower();
        if (component != null) component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.followers)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel.getData() != null) {
            view().setData(viewModel.getData());
        } else {
            loadData();
        }
    }

    public void openProfile(long id) {
        rootPresenter.showProfile(String.valueOf(id));
    }

    private void loadData() {
        Disposable d = repository.getFollowers(getId())
                .subscribe(res -> {
                    viewModel.setData(res);
                    view().setData(res);
                }, this::showError);

        compositeDisposable.add(d);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_followers;
    }

    private long getId() {
        return getArgs().getLong(ID_KEY);
    }

    @dagger.Subcomponent
    @DaggerScope(FollowersController.class)
    public interface Component {
        void inject(FollowersController controller);

    }

}
