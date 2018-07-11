package im.goody.android.screens.intro.resources;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;
import im.goody.android.screens.choose_help.ChooseHelpViewModel;
import im.goody.android.ui.dialogs.EditTextDialog;
import io.reactivex.disposables.Disposable;

public class ResourcesController extends BaseController<ResourcesView> {
    private ChooseHelpViewModel viewModel;

    public ResourcesController(ChooseHelpViewModel viewModel) {
        super();
        this.viewModel = viewModel;
    }

    public ResourcesController(Bundle args) {
        super(args);
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);

        if (viewModel != null) {
            view().setData(viewModel);
        }
    }


    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusResources();
        if (component != null)
            component.inject(this);
    }
    @Override
    protected void initActionBar() {
        // do nothing, we're in child controller
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.intro_resources;
    }



    //endregion

    void removeTag(String tag) {
        int position = viewModel.tags.indexOf(tag);
        viewModel.tags.remove(position);
        view().removeTag(position);
    }

    void addTag() {
        Disposable disposable = new EditTextDialog(R.string.choose_tag_title).show(getActivity())
                .subscribe(tag -> {
                    if (!viewModel.tags.contains(tag)) {
                        viewModel.tags.add(tag);
                        view().addTag(tag);
                    } else {
                        view().showMessage(R.string.tag_already_present);
                    }
                });

        compositeDisposable.add(disposable);
    }

    //region ================= DI =================
    @dagger.Subcomponent()
    @DaggerScope(ResourcesController.class)
    public interface Component {

        void inject(ResourcesController controller);

    }
    //endregion
}
