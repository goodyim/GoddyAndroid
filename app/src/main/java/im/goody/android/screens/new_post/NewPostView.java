package im.goody.android.screens.new_post;

import android.content.Context;
import android.util.AttributeSet;

import im.goody.android.core.BaseView;
import im.goody.android.databinding.ScreenNewPostBinding;

public class NewPostView extends BaseView<NewPostController, ScreenNewPostBinding> {
    public NewPostView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setData(NewPostViewModel model) {
        binding.setPost(model);
    }

    @Override
    protected void onAttached() {
        binding.newPostAddLocation.setOnClickListener(v -> controller.chooseLocation());
        binding.newPostAddPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newPostPhoto.setOnClickListener(v -> controller.choosePhoto());
        binding.newPostLocationClear.setOnClickListener(v -> controller.clearLocation());
    }

    @Override
    protected void onDetached() {
    }
}
