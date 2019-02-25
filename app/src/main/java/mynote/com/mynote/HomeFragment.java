package mynote.com.mynote;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mynote.com.mynote.Adapter.ViewPageAdapter;
import mynote.com.mynote.Fragment.BalanceFragment;
import mynote.com.mynote.Fragment.NoteFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPageAdapter adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tablayout);
        viewPager = view.findViewById(R.id.viewpager);
        adapter = new ViewPageAdapter(getFragmentManager());

        adapter.addFragment(new NoteFragment(),"QEYDLƏRİM");
        adapter.addFragment(new BalanceFragment(),"BALANS");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;
    }


}
