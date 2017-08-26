package im.goody.android.screens.intro;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class IntroController extends BaseController<IntroView> {
    private IntroPageAdapter adapter;

    public IntroController() {
        super();
        adapter = new IntroPageAdapter(this);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setAdapter(adapter);
    }

    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusIntro();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(false)
                .setBackArrow(true)
                .setStatusBarVisible(false)
                .build();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_intro, container, false);
    }

    void close() {
        if (repository.isSigned()) {
            rootPresenter.showMainScreen(true);
        } else {
            rootPresenter.showLoginScreen(true);
        }
    }

    //endregion

    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(IntroController.class)
    public interface Component {
        void inject(IntroController controller);
    }

    //endregion
}
