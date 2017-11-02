package im.goody.android.screens.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;
import io.reactivex.Observable;

import static im.goody.android.Constants.ID_NONE;

public class MainController extends BaseController<MainView> implements MainAdapter.MainItemHandler {
    private MainViewModel viewModel = new MainViewModel();
    private boolean findItems = true;
    private static final String USER_ID_KEY = "MainController.id";
    private static final String SHOW_ARROW_KEY = "MainController.showArrow";
    private static final String EVENTS_ONLY = "MainController.eventsOnly";

    private static final boolean SHOW_ARROW_NONE = false;

    public MainController(Long id, Boolean showArrow, Boolean eventsOnly) {
        super(new BundleBuilder()
                .putLong(USER_ID_KEY, id)
                .putBoolean(SHOW_ARROW_KEY, showArrow)
                .putBoolean(EVENTS_ONLY, eventsOnly)
                .build());
    }

    public MainController(Long id, Boolean showArrow) {
        super(new BundleBuilder()
                .putLong(USER_ID_KEY, id)
                .putBoolean(SHOW_ARROW_KEY, showArrow)
                .putBoolean(EVENTS_ONLY, false)
                .build());
    }

    public MainController() {
        super(new Bundle());
    }

    public MainController(Bundle bundle) {
        super(bundle);
    }

    // ======= region MainController =======

    void refreshData() {
        Observable<List<Deal>> observable;

        if (isEventsOnly()) {
            observable = repository.getEvents(getId(), viewModel.resetPageAndGet());
        } else {
            observable = repository.getPosts(getId(), viewModel.resetPageAndGet());
        }

        disposable = convertDealsToModels(observable)
                .subscribe(
                        result -> {
                            findItems = true;
                            viewModel.setData(result);
                            view().showData(result);
                            view().addScrollListener();
                        },
                        error -> {
                            view().finishLoading();
                            view().showErrorWithRetry(error.getMessage(), v -> {
                                view().startLoading();
                                refreshData();
                            });
                        }
                );
    }

    void loadMore() {
        if (!findItems) {
            view().finishLoading();
            return;
        }

        findItems = false;

        Observable<List<Deal>> observable;

        if (isEventsOnly()) {
            observable = repository.getEvents(getId(), viewModel.incrementPageAndGet());
        } else {
            observable = repository.getPosts(getId(), viewModel.incrementPageAndGet());
        }

        disposable = convertDealsToModels(observable)
                .subscribe(
                        result -> {
                            viewModel.addData(result);
                            view().addData(result);
                            if (result.size() > 0)
                                findItems = true;
                        },
                        error -> {
                            viewModel.decrementPage();
                            view().finishLoading();
                            view().showErrorWithRetry(getErrorMessage(error), v -> loadMore());
                        },
                        () -> view().addScrollListener()
                );
    }

    void showNewPostScreen() {
        rootPresenter.showNewPostScreen();
    }

    void showNewEventScreen() {
        rootPresenter.showNewEventScreen();
    }

    // endregion

    // ======= region MainItemHandler =======

    @Override
    public void report(long id) {
        repository.sendReport(id).subscribe(
                s -> view().showMessage(view().getContext().getString(R.string.report_submitted)),
                error -> view().showMessage(error.getMessage())
        );
    }

    @Override
    public void showDetail(long id) {
        rootPresenter.showDetailScreen(id);
    }

    @Override
    public void share(String text) {
        super.share(text);
    }

    @Override
    public void openMap(Location location) {
        if (location != null)
            openMap(location.getAddress());
    }

    @Override
    public void openProfile(long id) {
        rootPresenter.showProfile(id);
    }

    @Override
    public Observable<Deal> like(long id) {
        return repository.likeDeal(id)
                .doOnError(error ->
                        view().showMessage(getErrorMessage(error)));
    }

    @Override
    public Observable<ParticipateRes> changeParticipateState(long id) {
        return repository.changeParticipateState(id)
                .doOnError(error ->
                        view().showMessage(getErrorMessage(error)));
    }

    @Override
    public void openPhoto(String imageUrl) {
        rootPresenter.showPhotoScreen(imageUrl);
    }

    // end

    //region ================= BaseController =================

    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusMain();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(getTitleRes())
                .setHomeState(isShowArrow() ? BarBuilder.HOME_ARROW : BarBuilder.HOME_HAMBURGER)
                .build();
    }

    private Integer getTitleRes() {
        long id = getId();

        if (id == ID_NONE) return R.string.news_title;
        if (isIdMine()) return R.string.my_posts;
        return R.string.user_posts;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if (viewModel.getData() == null) refreshData();
        else {
            view().showData(viewModel.getData());
            view().scrollToPosition(viewModel.getPosition());
        }
        if (getId() != ID_NONE && !isIdMine()) view().hideNewButton();
    }

    @Override
    protected void onDetach(@NonNull View view) {
        viewModel.setPosition(view().getCurrentPosition());
        super.onDetach(view);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_main;
    }

    //endregion

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(MainController.class)
    public interface Component {

        void inject(MainController controller);
    }
    //endregion


    // ======= region private methods =======

    private Observable<List<MainItemViewModel>> convertDealsToModels(Observable<List<Deal>> original) {
        return original.flatMap(Observable::fromIterable)
                .map(MainItemViewModel::new)
                .toList()
                .toObservable();
    }

    @SuppressWarnings("ConstantConditions")
    private long getId() {
        return getArgs().getLong(USER_ID_KEY, ID_NONE);
    }

    private boolean isShowArrow() {
        return getArgs().getBoolean(SHOW_ARROW_KEY, SHOW_ARROW_NONE);
    }

    private boolean isIdMine() {
        return getId() == repository.getUserData().getUser().getId();
    }

    private boolean isEventsOnly() {
        return getArgs().getBoolean(EVENTS_ONLY);
    }

    //endregion
}
