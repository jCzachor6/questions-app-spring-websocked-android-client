package czachor.jakub.questions.app.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;

import czachor.jakub.questions.app.AnswersApplication;
import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.MessageType;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.QuestionsMessage;
import czachor.jakub.questions.app.utils.ViewPagerAdapter;

public class AppMainFragment extends Fragment {
    private List<QuestionDTO> questions = Collections.emptyList();
    private ViewPager viewPager;
    private ViewPagerAdapter myPagerAdapter;
    private TabLayout tabLayout;

    public AppMainFragment() {
    }

    public static AppMainFragment newInstance() {
        AppMainFragment f = new AppMainFragment();
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void subscribe(View view) {
        String role = AnswersApplication.instance().role();
        AnswersApplication.instance().getSubscriptions().addSubscription("/questions/" + role, topicMessage -> {
            questions = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<List<QuestionDTO>>() {
            }.getType());
            this.loadViewPager(view);
        });
        QuestionsMessage message = new QuestionsMessage(MessageType.ALL);
        AnswersApplication.instance().getStompClient().send("/topic/questions", message.json()).subscribe();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_main, container, false);
        this.subscribe(view);
        return view;
    }

    private void loadViewPager(View view) {
        getActivity().runOnUiThread(() -> {
            viewPager = view.findViewById(R.id.pager);
            myPagerAdapter = new ViewPagerAdapter(getFragmentManager(), questions);
            viewPager.setAdapter(myPagerAdapter);
            tabLayout = view.findViewById(R.id.tablayout);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
            tabLayout.setupWithViewPager(viewPager);
        });
    }
}
