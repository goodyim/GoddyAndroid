package im.goody.android.screens.main;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class MainController extends BaseController<MainView> implements MainAdapter.MainItemHandler {

    private MainViewModel viewModel = new MainViewModel();

    // ======= region MainController =======

    void refreshData() {
        disposable = repository.getNews(viewModel.resetPageAndGet())
                .subscribe(
                        result -> {
                            viewModel.setData(result);
                            view().showData(result);
                        },
                        error -> {
                            view().finishLoading();
                            view().showMessage(error.getMessage());
                        }
                );
    }

    void loadMore() {
        disposable = repository.getNews(viewModel.incrementPageAndGet())
                .subscribe(
                        result -> {
                            viewModel.addData(result);
                            view().addData(result);
                        },
                        error -> {
                            view().finishLoading();
                            view().showMessage(error.getMessage());
                        }
                );
    }

    void showNewPostScreen() {
        rootPresenter.showNewPostScreen();
    }

    // endregion

    // ======= region MainItemHandler =======

    @Override
    public void report(long id) {
        repository.sendReport(id).subscribe(
                s -> view().showMessage(getActivity().getString(R.string.report_submitted)),
                error -> view().showMessage(error.getMessage())
        );
    }

    @Override
    public void share(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(shareIntent);
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
                .setBackArrow(false)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        if (viewModel.getData() == null) refreshData();
        else view().showData(viewModel.getData());
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_main, container, false);
    }

    //endregion

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(MainController.class)
    public interface Component {
        void inject(MainController controller);
    }

    //endregion
}
