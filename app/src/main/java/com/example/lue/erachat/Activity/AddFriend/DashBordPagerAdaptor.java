package com.example.lue.erachat.Activity.AddFriend;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lue.erachat.Activity.FragmentProfile;
import com.example.lue.erachat.Activity.GroupChat.ChatList;
import com.example.lue.erachat.Activity.More.MoreFragment;

/**
 * Created by lue on 14-06-2017.
 */

public class DashBordPagerAdaptor extends FragmentPagerAdapter {

    final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[] { "Chat","Contact", "More","Me" };
    public DashBordPagerAdaptor(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatList();

            case 1:
                return new ChatList();

            case 2:
                return new MoreFragment();
            case 3:
                return new FragmentProfile();
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
