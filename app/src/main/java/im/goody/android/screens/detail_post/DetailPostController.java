package im.goody.android.screens.detail_post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import im.goody.android.Constants;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Comment;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.BundleBuilder;
import im.goody.android.utils.TextUtils;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

public class DetailPostController extends BaseController<DetailPostView>
        implements DetailPostAdapter.DetailPostHandler {
    private static final String ID_KEY = "DetailPostController.id";

    private DetailPostViewModel viewModel = new DetailPostViewModel();

    private MenuItem eventStateItem;
    private MenuItem editItem;
    private MenuItem deleteItem;

    public DetailPostController(Long id) {
        super(new BundleBuilder()
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

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_detail;
    }

    // endregion

    // ======= region DetailPostController =======

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.deal_menu, menu);

        eventStateItem = menu.add(Menu.NONE, 1, 0, "");
        editItem = menu.add(R.string.edit);
        deleteItem = menu.add(R.string.delete);

        eventStateItem.setOnMenuItemClickListener(item -> {
            finishEvent();
            return true;
        });
        editItem.setOnMenuItemClickListener(item -> {
            showEditScreen();
            return true;
        });

        deleteItem.setOnMenuItemClickListener(item -> {
            deletePost();
            return true;
        });

        if(viewModel.getBody() == null) {
            editItem.setVisible(false);
            eventStateItem.setVisible(false);
            deleteItem.setVisible(false);
        } else {
            updateMenu();
        }
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
            view().showData(viewModel, getCurrentUserId());
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
        Disposable d = repository.sendComment(viewModel.getId(), viewModel.getCommentObject())
                .subscribe(commentRes -> {
                    viewModel.addComment(commentRes);
                    viewModel.commentBody.set(null);
                    view().commentCreated();
                }, error -> {
                    view().hideCommentProgress();
                    showError(error);
                });
        compositeDisposable.add(d);
    }

    void loadData() {
        Disposable d = repository.getDeal(viewModel.getId())
                .subscribe(deal -> {
                    viewModel.setBody(deal);
                    view().showData(viewModel, getCurrentUserId());
                    updateMenu();
                }, error -> {
                    view().finishLoading();
                    view().showErrorWithRetry(error.getMessage(), v -> {
                        view().startLoading();
                        loadData();
                    });
                });
        compositeDisposable.add(d);
    }


    // endregion



    // ======= region DetailPostHandler =======
    @Override
    public void openParticipants(long id) {
        rootPresenter.showParticipants(id);
    }

    @Override
    public void share(Deal deal) {
        String text = TextUtils.buildShareText(viewModel.getBody().getDeal());
        super.share(text);
    }

    @Override
    public void openMap(Location location) {
        if (location != null)
            openMap(location.getAddress());
    }

    @Override
    public void openProfile(String id) {
        rootPresenter.showProfile(id);
    }

    @Override
    public Observable<ParticipateRes> changeParticipateState() {
        return repository.changeParticipateState(viewModel.getId())
                .doOnError(this::showError);
    }

    @Override
    public Observable<Deal> like() {
        Deal deal = viewModel.getBody().getDeal();

        return repository.likeDeal(deal.getId())
                .doOnError(error ->
                        view().showMessage(getErrorMessage(error)));
    }

    @Override
    public void openPhoto(String url) {
        rootPresenter.showPhotoScreen(url);
    }

    @Override
    public void reply(String author) {
        viewModel.commentBody.set(String.format(Constants.MENTION_FORMAT, author));
        view().showCommentFocus();
    }

    @Override
    public void deleteComment(int commentPosition) {
        Comment comment = viewModel.getDeal().getComments().get(commentPosition);
        Disposable d = repository.deleteComment(comment.getId())
                .subscribe(ignored -> {
                    viewModel.removeComment(commentPosition);
                    view().removeComment(commentPosition);
                    view().showMessage(R.string.comment_deleted);
                }, this::showError);
        compositeDisposable.add(d);
    }


    // endregion

    // ======= region private methods =======

    private int getCurrentUserId() {
        return repository.getCurrentUser().getId();
    }

    private void deletePost() {
        Disposable d  = repository.deletePost(viewModel.getId())
                .subscribe(response -> {
                    showToast(R.string.delete_success);
                    rootPresenter.showMain();
                }, this::showError);
        compositeDisposable.add(d);
    }

    private void report() {
        Disposable d = repository.sendReport(viewModel.getId()).subscribe(
                s -> view().showMessage(R.string.report_submitted),
                error -> view().showMessage(error.getMessage())
        );
        compositeDisposable.add(d);
    }

    private void showEditScreen() {
        Deal deal = viewModel.getDeal();

        if(deal.getEvent() == null) {
            rootPresenter.showEditPostScreen(deal);
        } else {
            rootPresenter.showEditEventScreen(deal);
        }
    }

    private void updateMenu() {
        Deal deal = viewModel.getDeal();

        if (deal.getEvent() != null && deal.isOwner() && deal.getEvent().isOpen()) {
            eventStateItem.setTitle(R.string.close_event);
            eventStateItem.setVisible(true);
        } else {
            eventStateItem.setVisible(false);
        }

        editItem.setVisible(deal.isOwner());
        deleteItem.setVisible(deal.isOwner());
    }

    @SuppressWarnings("CodeBlock2Expr")
    private void finishEvent() {
       rootPresenter.openFinishEvent(viewModel.getId());
    }

    // endregion

    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(DetailPostController.class)
    public interface Component {

        void inject(DetailPostController controller);
    }

    // endregion
}
