package im.goody.android.screens.intro.finish;

import android.os.Bundle;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;
import io.reactivex.disposables.Disposable;

public class IntroFinishController extends BaseController<IntroFinishView> {
    private ChooseHelpViewModel viewModel;

    public IntroFinishController(ChooseHelpViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public IntroFinishController(Bundle args) {
        super(args);
    }

    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusIntroFinish();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        // do nothing, we're in child controller
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.intro_finish;
    }

    public void confirm() {
        rootPresenter.showProgress(R.string.help_info_updating);
        Disposable disposable = repository.fillProfile(viewModel.body())
                .subscribe(res -> {
                    rootPresenter.hideProgress();
                    rootPresenter.showMain();
                }, error -> {
                    rootPresenter.hideProgress();
                    showError(error);
                });

        compositeDisposable.add(disposable);
    }

    //endregion


    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(IntroFinishController.class)
    public interface Component {
        void inject(IntroFinishController controller);
    }

    //endregion
}
