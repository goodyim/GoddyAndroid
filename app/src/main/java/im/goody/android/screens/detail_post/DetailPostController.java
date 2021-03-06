package im.goody.android.screens.detail_post;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.ParticipateRes;
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
            changeEventState();
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
                    updateMenu();
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
    public void openMap(Location location) {
        if (location != null)
            openMap(location.getAddress());
    }

    @Override
    public void openProfile(long id) {
        rootPresenter.showProfile(id);
    }

    @Override
    public Observable<ParticipateRes> changeParticipateState() {
        return repository.changeParticipateState(viewModel.getId())
                .doOnError(error ->
                        view().showMessage(getErrorMessage(error)));
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

    // endregion

    // ======= region private methods =======


    private void deletePost() {
        disposable = repository.deletePost(viewModel.getId())
                .subscribe(response -> {
                    showToast(R.string.delete_success);
                    rootPresenter.showNews();
                }, this::showError);
    }

    private void report() {
        disposable = repository.sendReport(viewModel.getId()).subscribe(
                s -> view().showMessage(getActivity().getString(R.string.report_submitted)),
                error -> view().showMessage(error.getMessage())
        );
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

        if (deal.getEvent() != null && deal.isOwner()) {
            eventStateItem.setTitle(getCloseOpenItemTitle());
            eventStateItem.setVisible(true);
        } else {
            eventStateItem.setVisible(false);
        }

        editItem.setVisible(deal.isOwner());
        deleteItem.setVisible(deal.isOwner());
    }

    @SuppressWarnings("CodeBlock2Expr")
    private void changeEventState() {
        disposable = repository.changeEventState(viewModel.getId())
                .subscribe(
                        eventStateRes -> {
                            viewModel.updateEventState(eventStateRes.isActive());
                            eventStateItem.setTitle(getCloseOpenItemTitle());

                            int msgRes = eventStateRes.isActive()
                                    ? R.string.event_opened
                                    : R.string.event_closed;

                            view().showMessage(msgRes);
                        }, this::showError);
    }

    @NonNull
    private String getCloseOpenItemTitle() {
        Deal.Event event = viewModel.getDeal().getEvent();

        int titleRes = event.isActive()
                ? R.string.close_event
                : R.string.open_event;
        return getActivity().getString(titleRes);
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
