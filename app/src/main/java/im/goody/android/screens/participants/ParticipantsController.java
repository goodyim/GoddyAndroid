package im.goody.android.screens.participants;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;
import io.reactivex.disposables.Disposable;

public class ParticipantsController extends BaseController<ParticipantsView> {
    private static final String ID_KEY = "ID_KEY";

    private ParticipantsViewModel viewModel = new ParticipantsViewModel();

    public ParticipantsController(Long postId) {
        super(new BundleBuilder()
                .putLong(ID_KEY, postId)
                .build());
    }

    public ParticipantsController(Bundle args) {
        super(args);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusParticipant();
        if (component != null) component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.participants_title)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_participants;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel.getData() == null) {
            loadData();
        } else {
            view().showData(viewModel.getData());
        }
    }

    public void itemClicked(long id) {
        rootPresenter.showProfile(String.valueOf(id));
    }

    private void loadData() {
        Disposable disposable = repository.getParticipants(getId())
                .subscribe(result -> {
                    viewModel.setData(result);
                    view().showData(result);
                }, this::showError);

        compositeDisposable.add(disposable);
    }

    private long getId() {
        return getArgs().getLong(ID_KEY);
    }

    @dagger.Subcomponent
    @DaggerScope(ParticipantsController.class)
    public interface Component {
        void inject(ParticipantsController controller);
    }
}
