package im.goody.android.screens.feedback;

import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Feedback;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import io.reactivex.disposables.Disposable;


public class FeedBackController extends BaseController<FeedBackView> {
    private FeedbackViewModel viewModel = new FeedbackViewModel();

    private static final int STATE_ALLOWED = 0;
    private static final int STATE_DENIED = 1;

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusFeedback();
        if (component != null) component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setTitleRes(R.string.feedback_title)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_feedback;
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

    private void loadData() {
        Disposable disposable = repository.getFeedback()
                .subscribe(result -> {
                    viewModel.setData(result);
                    view().showData(viewModel.getData());
                }, error -> {
                    view().showErrorWithRetry(error.getMessage(), v -> {
                        loadData();
                    });
                });

        compositeDisposable.add(disposable);
    }

    void openDetail(Feedback item) {
        rootPresenter.showDetailScreen(item.getObjectId());
    }

    public void openProfile(long id) {
        rootPresenter.showProfile(String.valueOf(id));
    }

    public void allowClicked(int position) {
        processRequest(STATE_ALLOWED, position);
    }

    public void denyClicked(int position) {
        processRequest(STATE_DENIED, position);
    }

    private void processRequest(int state, int position) {
        Feedback feedback = viewModel.getData().get(position);

        Disposable disposable = repository.processPhoneRequest(feedback.getObjectId(), state)
                .subscribe(result -> {
                    viewModel.removeFeedback(position);

                    if (view() != null) {
                        view().removeItem(position);

                        int messageRes = state == STATE_ALLOWED
                                ? R.string.request_allowed
                                : R.string.request_denied;

                        showMessage(messageRes);
                    }
                }, this::showError);

        compositeDisposable.add(disposable);
    }

    //======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(FeedBackController.class)
    public interface Component {
        void inject(FeedBackController controller);
    }

    //endregion
}
