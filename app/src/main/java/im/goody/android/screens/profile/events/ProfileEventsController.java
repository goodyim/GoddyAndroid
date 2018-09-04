package im.goody.android.screens.profile.events;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BundleBuilder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class ProfileEventsController extends BaseController<ProfileEventsView>
        implements ProfileEventsAdapter.ProfileEventItemHandler {
    private static final String USER_ID_KEY = "ProfileEventsController.userId";
    private static final String EVENT_STATE_KEY = "ProfileEventsController.eventState";

    private ProfileEventsViewModel viewModel = new ProfileEventsViewModel();

    public ProfileEventsController(String id, String eventsState) {
        super(new BundleBuilder()
                .putString(USER_ID_KEY, id)
                .putString(EVENT_STATE_KEY, eventsState)
                .build());
    }

    public ProfileEventsController(Bundle args) {
        super(args);
    }

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusProfileEvent();
        component.inject(this);
    }

    @Override
    protected void initActionBar() {
        // do nothing, we're in child controller
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.page_profile_events;
    }

    // ======= region ProfileEventItemHandler =======

    @Override
    public void showDetail(long id) {
        rootPresenter.showDetailScreen(id);
    }

    @Override
    public void share(String text) {
        super.share(text);
    }

    @Override
    public Observable<Deal> like(long id) {
        return repository.likeDeal(id)
                .doOnError(this::showError);
    }

    @Override
    public void openPhoto(String imageUrl) {
        rootPresenter.showPhotoScreen(imageUrl);
    }

    // end

    // ======= region ProfileEventsController =======


    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel.getEvents() == null)
            loadData();
        else
            view().showEvents(viewModel.getEvents());
    }

    // end

    // end

    // ======= region private methods =======

    private String getUserId() {
        return getArgs().getString(USER_ID_KEY);
    }

    private String getEventsState() {
        return getArgs().getString(EVENT_STATE_KEY);
    }

    // end

    private void loadData() {
        Disposable d = repository.getEvents(getUserId(), getEventsState())
                .flatMap(Observable::fromIterable)
                .map(ProfileEventItemViewModel::new)
                .toList()
                .toObservable()
                .subscribe(events -> {
                    viewModel.setEvents(events);
                    view().showEvents(events);
                }, error -> view().showMessage(getErrorMessage(error)));
        compositeDisposable.add(d);
    }

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(ProfileEventsController.class)
    public interface Component {

        void inject(ProfileEventsController controller);
    }

    // end
}
