package im.goody.android.screens.login;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class LoginController extends BaseController<LoginView> {

    private LoginViewModel loginData = new LoginViewModel();

    //======= region LoginController =======

    void login() {
        if (loginData.isValid()) {
            rootPresenter.showProgress(R.string.register_progress_title);
            disposable = repository.login(loginData.body()).subscribe(
                    result -> {
                        rootPresenter.hideProgress();
                        rootPresenter.showMainScreen();
                    },
                    error -> {
                        rootPresenter.hideProgress();
                        attachedView.showSnackbarMessage(error.getMessage());
                    }
            );
        } else {
            attachedView.showSnackbarMessage(R.string.invalid_fields_message);
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
                .setBackArrow(true)
                .setTitleRes(R.string.login_title)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        attachedView.setAuthData(loginData);
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
