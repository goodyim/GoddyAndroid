package im.goody.android.screens.login;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.helpers.BarBuilder;
import im.goody.android.utils.UIUtils;

public class LoginController extends BaseController<LoginView> {

    private LoginViewModel loginData = new LoginViewModel();

    //======= region LoginController =======

    void login() {
        if (loginData.isValid()) {
            UIUtils.hideKeyboard(getActivity());
            rootPresenter.showProgress(R.string.login_progress_title);
            disposable = repository.login(loginData.body()).subscribe(
                    result -> {
                        rootPresenter.hideProgress();
                        rootPresenter.showMainScreen(true);
                    },
                    error -> {
                        rootPresenter.hideProgress();
                        view().showMessage(error.getMessage());
                    }
            );
        } else {
            view().showMessage(R.string.invalid_fields_message);
        }
    }

    void goToRegister() {
        rootPresenter.showRegisterScreen();
    }

    //endregion

    //======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusLogin();
        if (component != null) component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setToolbarVisible(true)
                .setHomeState(BarBuilder.HOME_GONE)
                .setTitleRes(R.string.login_title)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setAuthData(loginData);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return inflater.inflate(R.layout.screen_login, container, false);
    }

    //endregion

    //======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(LoginController.class)
    public interface Component {
        void inject(LoginController controller);
    }

    //endregion
}
