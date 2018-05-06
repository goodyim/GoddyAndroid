package im.goody.android.screens.register;

import android.Manifest.permission;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.data.validation.ValidateResult;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.choose_help.ChooseHelpController;
import im.goody.android.ui.dialogs.ChooseImageOptionsDialog;
import im.goody.android.ui.dialogs.DatePickDialog;
import im.goody.android.ui.dialogs.OptionsDialog;
import im.goody.android.ui.helpers.BarBuilder;

public class RegisterController extends BaseController<RegisterView> {
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CAMERA_REQUEST = 1;

    private static final int PERMISSION_CODE = 200;
    private static final String[] PERMISSIONS = {permission.READ_EXTERNAL_STORAGE};

    private OptionsDialog dialog = new ChooseImageOptionsDialog();
    private RegisterViewModel viewModel = new RegisterViewModel();

    // ======= region BaseController =======

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusRegister();
        component.inject(this);
    }

    @Override
    protected void initActionBar() {
        rootPresenter.newBarBuilder()
                .setHomeState(BarBuilder.HOME_GONE)
                .setStatusBarVisible(false)
                .setToolbarVisible(false)
                .setTitleRes(R.string.register_title)
                .build();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.screen_register;
    }

    //endregion

    // ======= region RegisterController =======

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        view().setData(viewModel);
    }

    void register() {
        ValidateResult validateResult = viewModel.validate();
        if (validateResult.isValid()) {
            rootPresenter.showProgress(R.string.register_progress_title);
            disposable = repository.register(viewModel.body())
                    .subscribe(
                            result -> {
                                rootPresenter.hideProgress();
                                rootPresenter.showChooseHelp(ChooseHelpController.MODE_SETUP);
                            },
                            error -> {
                                rootPresenter.hideProgress();
                                showError(error);
                            }
            );
        } else {
            showError(validateResult);
        }
    }

    void chooseDate() {
        Context context = getActivity();
        DatePickDialog.with(context).show(viewModel.birthday.get())
                .subscribe(calendar -> viewModel.birthday.set(calendar));
    }

    void redirectToLogin() {
        getActivity().onBackPressed();
    }

    //endregion


    // ======= region DI =======

    @dagger.Subcomponent
    @DaggerScope(RegisterController.class)
    public interface Component {
        void inject(RegisterController controller);
    }

    //endregion
}
