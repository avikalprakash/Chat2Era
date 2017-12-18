package com.example.lue.erachat.Activity.Events;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lue.erachat.Activity.More.MoreFragment;
import com.example.lue.erachat.Activity.Task.TaskList;

/**
 * Created by lue on 14-06-2017.
 */

public class EventPagerAdaptor extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Today", "Upcoming", "Past" };

    public EventPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new EventList();

            case 1:
                return new EventList();
            case 2:
                return new EventList();
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
