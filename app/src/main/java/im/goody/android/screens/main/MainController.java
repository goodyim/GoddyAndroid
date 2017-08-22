package im.goody.android.screens.main;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import dagger.Provides;
import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.dto.Deal;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import io.reactivex.disposables.Disposable;

public class MainController extends BaseController<MainView> {

    @Inject
    protected MainModel model;

    private Disposable disposable;
    private List<Deal> data;

    public void refreshData() {
        disposable = model.getNews().subscribe(result -> {
            data = result;
            attachedView.showData(data);
        });
    }

    //region ================= BaseController =================

    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusMain(new Module());
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
        if (data == null) refreshData();
        else attachedView.showData(data);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_main, container, false);
    }

    //endregion

    //region ================= DI =================

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(MainController.class)
        MainModel providesMainModel() {
            return new MainModel();
        }
    }

    @dagger.Subcomponent(modules = MainController.Module.class)
    @DaggerScope(MainController.class)
    public interface Component {
        void inject(MainController controller);
    }

    //endregion

    void onNextClick() {
        rootPresenter.showIntroScreen();
    }
}
