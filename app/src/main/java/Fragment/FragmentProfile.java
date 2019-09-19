//package Fragment;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//
//import com.ratna.foosip.Home;
//import com.ratna.foosip.R;
//
///**
// * Created by ratna on 9/7/2016.
// */
//public class FragmentProfile extends Fragment {
//
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_enter_profile, container, false);
//
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        LinearLayout ll_next = (LinearLayout) getView().findViewById(R.id.ll_next);
//        ll_next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//               Intent intent = new Intent(getActivity(), Home.class);
//                startActivity(intent);
//
//            }
//        });
//
//
//    }
//}
//
