package im.goody.android.screens.news;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.data.dto.Event;
import im.goody.android.data.dto.Location;
import im.goody.android.data.network.res.ParticipateRes;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.BundleBuilder;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

import static im.goody.android.Constants.ID_NONE;

public class NewsController extends BaseController<NewsView> implements NewsAdapter.MainItemHandler {
    private NewsViewModel viewModel = new NewsViewModel();
    private boolean findItems = true;
    private static final String USER_ID_KEY = "NewsController.id";
    private static final String IS_ROOT = "NewsController.isRoot";
    private static final String CONTENT_TYPE = "NewsController.contentType";

    private static final boolean IS_ROOT_NONE = false;

//    public static final String CONTENT_EVENTS = "event";
//    public static final String CONTENT_POSTS = "deal";

    public static final String CONTENT_FEED = "feed";
    public static final String CONTENT_LOCATION = "location";
    public static final String CONTENT_All = "all";

    public NewsController(String contentType, Boolean isRoot, String id) {
        super(new BundleBuilder()
                .putString(USER_ID_KEY, id)
                .putBoolean(IS_ROOT, isRoot)
                .putString(CONTENT_TYPE, contentType)
                .build());
    }

    public NewsController(String contentType, Boolean isRoot) {
        super(new BundleBuilder()
                .putString(CONTENT_TYPE, contentType)
                .putBoolean(IS_ROOT, isRoot)
                .build());
    }

    public NewsController() {
        super(new Bundle());
    }

    public NewsController(Bundle bundle) {
        super(bundle);
    }

    // ======= region NewsController =======

    void refreshData() {
        Observable<List<Deal>> observable = repository.getPosts(
                getId(),
                getContentType(),
                viewModel.resetPageAndGet());


        Disposable disposable = convertDealsToModels(observable)
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

        compositeDisposable.add(disposable);
    }

    void loadMore() {
        if (!findItems) {
            view().finishLoading();
            return;
        }

        findItems = false;

        Observable<List<Deal>> observable = repository.getPosts(
                getId(),
                getContentType(),
                viewModel.incrementPageAndGet());

        Disposable disposable = convertDealsToModels(observable)
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

        compositeDisposable.add(disposable);
    }

    // endregion

    // ======= region MainItemHandler =======

    @Override
    public void report(long id) {
        Disposable disposable = repository.sendReport(id).subscribe(
                s -> view().showMessage(view().getContext().getString(R.string.report_submitted)),
                error -> view().showMessage(error.getMessage())
        );
        compositeDisposable.add(disposable);
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
    public void openProfile(String id) {
        rootPresenter.showProfile(id);
    }

    @Override
    public Observable<Deal> like(long id) {
        return repository.likeDeal(id)
                .doOnError(this::showError);
    }

    @Override
    public Observable<ParticipateRes> changeParticipateState(long id) {
        return repository.changeParticipateState(id)
                .doOnError(this::showError);
    }

    @Override
    public void finishEvent(long id) {
        rootPresenter.openFinishEvent(id);
    }

    @Override
    public Observable<Event.PhoneInfo> requestPhone(long dealId) {
        return repository.requestPhone(dealId)
                .doOnError(this::showError);
    }

    @Override
    public Observable<ResponseBody> deletePost(long id) {
        return repository.deletePost(id)
                .doOnError(this::showError);
    }

    @Override
    public void showEdit(Deal deal) {
        if (deal.getEvent() == null)
            rootPresenter.showEditPostScreen(deal);
        else
            rootPresenter.showEditEventScreen(deal);
    }

    @Override
    public void openPhoto(String imageUrl) {
        rootPresenter.showPhotoScreen(imageUrl);
    }

    @Override
    public void openParticipants(long id) {
        rootPresenter.showParticipants(id);
    }

    // end

    //region ================= BaseController =================

    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusNews();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        if (isRoot()) {
            rootPresenter.newBarBuilder()
                    .setToolbarVisible(true)
                    .setTitleRes(getTitleRes())
                    .setHomeState(isRoot() ? BarBuilder.HOME_ARROW : BarBuilder.HOME_HAMBURGER)
                    .build();
        }
    }

    private Integer getTitleRes() {
        if (isIdMine())
            return R.string.my_posts;
        else
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
    }

    @Override
    protected void onDetach(@NonNull View view) {
        viewModel.setPosition(view().getCurrentPosition());
        super.onDetach(view);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_news;
    }

    //endregion

    //region ================= DI =================

    @dagger.Subcomponent
    @DaggerScope(NewsController.class)
    public interface Component {

        void inject(NewsController controller);
    }
    //endregion


    // ======= region private methods =======

    private Observable<List<NewsItemViewModel>> convertDealsToModels(Observable<List<Deal>> original) {
        return original.flatMap(Observable::fromIterable)
                .map(NewsItemViewModel::new)
                .toList()
                .toObservable();
    }

    @SuppressWarnings("ConstantConditions")
    private String getId() {
        return getArgs().getString(USER_ID_KEY, ID_NONE);
    }

    private boolean isRoot() {
        return getArgs().getBoolean(IS_ROOT, IS_ROOT_NONE);
    }

    private boolean isIdMine() {
        return getId().equals(String.valueOf(repository.getCurrentUser().getId()));
    }

    private String getContentType() {
        return getArgs().getString(CONTENT_TYPE);
    }

    //endregion
}
