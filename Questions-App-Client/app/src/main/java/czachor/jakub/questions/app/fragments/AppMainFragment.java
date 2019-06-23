package czachor.jakub.questions.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Collections;
import java.util.List;

import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.utils.ViewPagerAdapter;

public class AppMainFragment extends Fragment {
    private List<QuestionDTO> questions = Collections.emptyList();

    public AppMainFragment() {
    }

    public static AppMainFragment newInstance() {
        AppMainFragment f = new AppMainFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_main, container, false);
        return view;
    }

    private void loadViewPager(View view) {
        ViewPager viewPager = view.findViewById(R.id.pager);
        ViewPagerAdapter myPagerAdapter = new ViewPagerAdapter(getFragmentManager(), questions);
        viewPager.setAdapter(myPagerAdapter);
        TabLayout tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
