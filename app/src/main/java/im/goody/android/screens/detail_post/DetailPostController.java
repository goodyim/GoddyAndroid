package im.goody.android.screens.detail_post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.ui.helpers.BundleBuilder;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;

public class DetailPostController extends BaseController<DetailPostView>
        implements DetailPostAdapter.DetailPostHandler {
    private static final String ID_KEY = "DetailPostController.id";

    private DetailPostViewModel viewModel = new DetailPostViewModel();

    public DetailPostController(long id) {
        super(new BundleBuilder(new Bundle())
                .putLong(ID_KEY, id)
                .build());
    }

    public DetailPostController(Bundle args) {
        super(args);
    }

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusDetailPost();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_ARROW)
                .setTitleRes(R.string.detail_title)
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_detail, container, false);
    }

    // endregion

    // ======= region DetailPostController =======

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.deal_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_report) {
            report();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        setHasOptionsMenu(true);
        view().setData(viewModel);
        viewModel.setId(getArgs().getLong(ID_KEY));

        if (viewModel.getBody() != null) {
            view().showData(viewModel);
        } else {
            loadData();
        }
    }

    @Override
    protected void onDetach(@NonNull View view) {
        viewModel.setPosition(view().getCurrentPosition());
        super.onDetach(view);
    }

    void sendComment() {
        view().showCommentProgress();
        disposable = repository.sendComment(viewModel.getId(), viewModel.getCommentObject())
                .subscribe(commentRes -> {
                    viewModel.addComment(commentRes);
                    viewModel.commentBody.set(null);
                    view().commentCreated();
                }, error -> {
                    view().hideCommentProgress();
                    showError(error);
                });
    }

    void loadData() {
        disposable = repository.getDeal(viewModel.getId())
                .subscribe(deal -> {
                    viewModel.setBody(deal);
                    view().showData(viewModel);
                }, error -> {
                    view().finishLoading();
                    view().showErrorWithRetry(error.getMessage(), v -> {
                        view().startLoading();
                        loadData();
                    });
                });
    }

    // endregion

    // ======= region DetailPostHandler =======

    @Override
    public void share(Deal deal) {
        String text = TextUtils.buildShareText(viewModel.getBody().getDeal());
        super.share(text);
    }

    @Override
    public Observable<Deal> like() {
        Deal deal = viewModel.getBody().getDeal();

        return repository.likeDeal(deal.getId())
                .doOnError(error ->
                        view().showMessage(getErrorMessage(error)));
    }

    // endregion

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(DetailPostController.class)
    public interface Component {
        void inject(DetailPostController controller);
    }

    // endregion

    // ======= region private methods =======

    private void report() {
        disposable = repository.sendReport(viewModel.getId()).subscribe(
                s -> view().showMessage(getActivity().getString(R.string.report_submitted)),
                error -> view().showMessage(error.getMessage())
        );
    }

    // endregion
}
