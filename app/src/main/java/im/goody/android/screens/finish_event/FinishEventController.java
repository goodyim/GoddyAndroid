package im.goody.android.screens.finish_event;

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

public class FinishEventController extends BaseController<FinishEventView> {
    private FinishEventViewModel viewModel = new FinishEventViewModel();

    private static final String EXTRA_ID_KEY = "EXTRA_ID";

    FinishEventController(Long id) {
        this(new BundleBuilder()
                .putLong(EXTRA_ID_KEY, id)
                .build());
    }

    public FinishEventController(Bundle build) {
        super(build);
    }

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusFinishEvent();
        component.inject(this);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        view().setData(viewModel);

        if (viewModel.getParticipants() == null) {
            loadData();
        }
    }

    void removeParticipant(int position) {
        viewModel.removeParticipant(position);
    }

    private void loadData() {
        Disposable d = repository.getParticipants(getEventId())
                .subscribe(result -> viewModel.setParticipants(result), this::showError);

        compositeDisposable.add(d);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.finish_event_head)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_finish_event;
    }

    private long getEventId() {
        return getArgs().getLong(EXTRA_ID_KEY);
    }

    public void send() {
        Disposable d = repository.finishEvent(getEventId(), viewModel.body())
                .subscribe(eventStateRes -> {
                    getRouter().popCurrentController();
                    showToast(R.string.event_closed);
                }, this::showError);

        compositeDisposable.add(d);
    }

    public void showProfile(long id) {
        rootPresenter.showProfile(String.valueOf(id));
    }

    @dagger.Subcomponent
    @DaggerScope(FinishEventController.class)
    public interface Component {
        void inject(FinishEventController controller);

    }
}
