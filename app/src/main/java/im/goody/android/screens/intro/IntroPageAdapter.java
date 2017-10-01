package im.goody.android.screens.intro;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import im.goody.android.R;
import im.goody.android.databinding.ItemIntroBinding;

class IntroPageAdapter extends PagerAdapter {

    private int[] pages = {R.drawable.intro_1, R.drawable.intro_2, R.drawable.intro_3,
            R.drawable.intro_4, R.drawable.intro_5};

    @Override
    public int getCount() {
        return pages.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.item_intro, container, false);
        ItemIntroBinding binding = DataBindingUtil.bind(view);
        IntroPage page = new IntroPage();
        page.setImage(pages[position]);
        binding.setPage(page);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}