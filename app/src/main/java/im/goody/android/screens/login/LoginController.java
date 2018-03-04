package im.goody.android.screens.login;

import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.ui.dialogs.EditTextDialog;
import im.goody.android.ui.helpers.BarBuilder;

public class LoginController extends BaseController<LoginView> {

    private LoginViewModel loginData = new LoginViewModel();

    //======= region LoginController =======

    void login() {
        if (loginData.isValid()) {
            rootPresenter.showProgress(R.string.login_progress_title);
            disposable = repository.login(loginData.body()).subscribe(
                    result -> {
                        rootPresenter.hideProgress();
                        rootPresenter.showNews();
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

    void forgotPasswordClicked() {
        new EditTextDialog(R.string.forgot_password_title)
                .show(getActivity())
                .subscribe(this::recoverPassword);
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
                .setToolbarVisible(false)
                .setStatusBarVisible(false)
                .setHomeState(BarBuilder.HOME_GONE)
                .setTitleRes(R.string.login_title)
                .build();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setAuthData(loginData);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_login;
    }

    //endregion

    // ======= region private methods =======

    private void recoverPassword(String email) {
        repository.recoverPassword(email)
                .subscribe(ignored -> {
                    if (getActivity() != null) {
                        String message = getActivity().getString(R.string.recover_instructions_sent, email);
                        view().showMessage(message);
                    }
                }, this::showError);
    }

    // end

    //======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(LoginController.class)
    public interface Component {
        void inject(LoginController controller);
    }

    //endregion
}
