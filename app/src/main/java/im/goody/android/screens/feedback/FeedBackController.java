package im.goody.android.screens.feedback;

import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Feedback;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;


public class FeedBackController extends BaseController<FeedBackView> {
    private FeedbackViewModel viewModel = new FeedbackViewModel();

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
        repository.getFeedback()
                .subscribe(result -> {
                    viewModel.setData(result);
                    view().showData(viewModel.getData());
                }, error -> {
                    view().showErrorWithRetry(error.getMessage(), v -> {
                        loadData();
                    });
                });
    }

    void openDetail(Feedback item) {
        rootPresenter.showDetailScreen(item.getObjectId());
    }

    //======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(FeedBackController.class)
    public interface Component {
        void inject(FeedBackController controller);
    }

    //endregion
}
