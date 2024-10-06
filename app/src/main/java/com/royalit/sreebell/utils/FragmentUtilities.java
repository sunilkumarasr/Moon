package com.royalit.sreebell.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.royalit.sreebell.R;


public class FragmentUtilities {


    public static void replaceFragment(FragmentActivity context, Fragment fragment, String currentFragment) {

        String fragmentTag = fragment.getClass().getName();
        FragmentManager manager = context.getSupportFragmentManager();


        boolean fragmentPopped = manager.popBackStackImmediate(fragmentTag, 0);


        if (!fragmentPopped && manager.findFragmentByTag(fragmentTag) == null) { //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_home_screen, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(currentFragment);


            ft.commit();
        } else {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.nav_host_fragment_content_home_screen, fragment, fragmentTag);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.addToBackStack(null);

            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
        }

    }

}
