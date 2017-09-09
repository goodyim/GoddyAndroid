package im.goody.android.screens.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import io.reactivex.Observable;

public class MainController extends BaseController<MainView> implements MainAdapter.MainItemHandler {

    private MainViewModel viewModel = new MainViewModel();
    private boolean findItems = true;

    // ======= region MainController =======

    void refreshData() {
        disposable = convertDealsToModels(
                repository.getNews(viewModel.resetPageAndGet()))
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
        disposable = convertDealsToModels(
                repository.getNews(viewModel.incrementPageAndGet()))
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
                .setTitleRes(R.string.main_title)
                .setHomeState(BarBuilder.HOME_HAMBURGER)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if (viewModel.getData() == null) refreshData();
        else {
            view().showData(viewModel.getData());
            view().scrollToPosition(viewModel.getPosition());
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        viewModel.setPosition(view().getCurrentPosition());
        super.onDetach(view);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_main, container, false);
    }

    //endregion

    private Observable<List<MainItemViewModel>> convertDealsToModels(Observable<List<Deal>> original) {
        return original.flatMap(Observable::fromIterable)
                .map(MainItemViewModel::new)
                .toList()
                .toObservable();
    }

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(MainController.class)
    public interface Component {
        void inject(MainController controller);
    }

    //endregion
}
