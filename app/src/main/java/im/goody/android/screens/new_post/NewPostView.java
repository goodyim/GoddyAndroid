package im.goody.android.screens.new_post;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNewPostBinding;
import im.goody.android.utils.UIUtils;

public class NewPostView extends BaseView<NewPostController, ScreenNewPostBinding> {
    public NewPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(NewPostViewModel model) {
        binding.setPost(model);
    }

    @Override
    protected void onAttached() {
        binding.newPostAddPlace.setOnClickListener(v -> controller.chooseLocation());
        binding.newPostAddPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newPostPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newPostLocationClear.setOnClickListener(v -> controller.clearLocation());
        binding.newPhotoSend.setOnClickListener(v -> {
            controller.sendData();
            UIUtils.hideKeyboard(getFocusedChild());
        });
        binding.newPostClearImage.setOnClickListener(v -> controller.clearPhoto());

        UIUtils.showKeyboard(binding.newPostDescription);
    }

    @Override
    protected void onDetached() {
    }
}
