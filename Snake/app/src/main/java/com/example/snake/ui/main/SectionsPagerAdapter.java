package com.example.snake.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.snake.Fragment1;
import com.example.snake.Fragment2;
import com.example.snake.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private static final  String[] TAB_NAME = new String[]{"Music setting","Game level setting"};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        return PlaceholderFragment.newInstance(position + 1);
        if(position == 0 ) return new Fragment1();
        else if(position == 1) return new Fragment2();
        else return null;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_NAME[position];
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_NAME.length;
    }
}