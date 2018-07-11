package im.goody.android.screens.intro.greet;

import im.goody.android.R;
import im.goody.android.core.BaseController;
import im.goody.android.di.DaggerScope;
import im.goody.android.di.components.RootComponent;

public class GreetController extends BaseController<GreetView> {
    //region ================= BaseController =================

    @Override
    protected void initDaggerComponent(RootComponent parentComponent) {
        Component component = parentComponent.plusGreet();
        if (component != null)
            component.inject(this);
    }

    @Override
    protected void initActionBar() {
        // do nothing, we're in child controller
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.intro_greet;
    }

    //endregion


    //region ================= DI =================

    @dagger.Subcomponent()
    @DaggerScope(GreetController.class)
    public interface Component {
        void inject(GreetController controller);
    }

    //endregion
}
