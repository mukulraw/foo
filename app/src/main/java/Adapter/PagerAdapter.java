//package Adapter;
//
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//
//import Fragment.FragmentMobile;
//import Fragment.FragmentOtp;
//import Fragment.FragmentProfile;
//
///**
// * Created by Vikas on 10/22/2015.
// */
//public class PagerAdapter extends FragmentStatePagerAdapter {
//    int mNumOfTabs;
//
//    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
//        super(fm);
//        this.mNumOfTabs = NumOfTabs;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//
//        switch (position) {
//            case 0:
//                FragmentMobile tab1 = new FragmentMobile();
//                return tab1;
//            case 1:
//                FragmentOtp tab2 = new FragmentOtp();
//                return tab2;
//
//            case 2:
//                FragmentProfile tab3 = new FragmentProfile();
//                return tab3;
//
//
//
//            default:
//                return null;
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return mNumOfTabs;
//    }
//}
