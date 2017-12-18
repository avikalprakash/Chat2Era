package com.example.lue.erachat.Activity.Task;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by lue on 14-06-2017.
 */

public class TaskPagerAdaptor extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "today", "This Week", "This Month","Completed" };

    public TaskPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new TaskList();

            case 1:
                return new TaskList();

            case 2:
                return new TaskList();
            case 3:
                return new TaskList();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }
}
