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

public class MainController extends BaseController<MainView> {

    private List<Deal> data;

    void refreshData() {
        disposable = repository.getNews().subscribe(result -> {
            data = result;
            attachedView.showData(data);
        });
    }

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
        if (data == null) refreshData();
        else attachedView.showData(data);
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
