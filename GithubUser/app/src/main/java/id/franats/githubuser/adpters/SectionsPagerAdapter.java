package id.franats.githubuser.adpters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.Objects;

import id.franats.githubuser.FollowerFragment;
import id.franats.githubuser.FollowingFragment;
import id.franats.githubuser.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private final String mNane;

    public SectionsPagerAdapter(Context context, FragmentManager fm, String name) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
        mNane = name;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.follower,
            R.string.followimg
    };

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = FollowerFragment.newInstance(mNane);
                break;
            case 1:
                fragment = FollowingFragment.newInstance(mNane);
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

}
