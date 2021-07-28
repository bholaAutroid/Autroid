package com.autroidbusiness.mynavigation.sg.fragment.navigation;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.autroidbusiness.mynavigation.R;
import com.autroidbusiness.mynavigation.sg.Main2Activity;
import com.autroidbusiness.mynavigation.sg.fragment.FragmentACtion;
import com.autroidbusiness.mynavigation.sg.fragment.FragmentDrama;
import com.autroidbusiness.mynavigation.sg.fragment.FragmentMusical;
import com.autroidbusiness.mynavigation.sg.fragment.FragmentThriller;


/**
 * @author msahakyan
 */

public class FragmentNavigationManager implements NavigationManager {

    private static FragmentNavigationManager sInstance;

    private FragmentManager mFragmentManager;
    private Main2Activity mActivity;

    public static FragmentNavigationManager obtain(Main2Activity activity) {
        if (sInstance == null) {
            sInstance = new FragmentNavigationManager();
        }
        sInstance.configure(activity);
        return sInstance;
    }

    private void configure(Main2Activity activity) {
        mActivity = activity;
        mFragmentManager = mActivity.getSupportFragmentManager();
    }

//    @Override
//    public void showFragmentAction(String title) {
//        showFragment( FragmentACtion.newInstance(title), false);
//    }
//
//    @Override
//    public void showFragmentComedy(String title) {
////        showFragment( FragmentComedy.newInstance(title), false);
//    }
//
//    @Override
//    public void showFragmentDrama(String title) {
//        showFragment( FragmentDrama.newInstance(title), false);
//    }
//
//    @Override
//    public void showFragmentMusical(String title) {
//        showFragment( FragmentMusical.newInstance(title), false);
//    }
//
//    @Override
//    public void showFragmentThriller(String title) {
//        showFragment( FragmentThriller.newInstance(title), false);
//    }

    private void showFragment(Fragment fragment, boolean allowStateLoss) {
        FragmentManager fm = mFragmentManager;

        @SuppressLint("CommitTransaction")
        FragmentTransaction ft = fm.beginTransaction()
            .replace( R.id.container, fragment);

        ft.addToBackStack(null);

//        if (allowStateLoss || !BuildConfig.DEBUG) {
//            ft.commitAllowingStateLoss();
//        } else {
//            ft.commit();
//        }

        fm.executePendingTransactions();
    }
}
