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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import czachor.jakub.questions.app.AnswersApplication;
import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.MessageType;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.QuestionsMessage;
import czachor.jakub.questions.app.utils.ViewPagerAdapter;

public class AppMainFragment extends Fragment {
    private List<QuestionDTO> questions = new ArrayList<>();
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
            System.out.println(topicMessage);
            List<QuestionDTO> retrieved = new Gson().fromJson(topicMessage.getPayload(), new TypeToken<List<QuestionDTO>>() {
            }.getType());
            if (role.equals("USER")) {
                if (retrieved.size() > 0) {
                    QuestionDTO questionDTO = findDifference(retrieved, questions);
                    getActivity().runOnUiThread(() -> myPagerAdapter.addElement(questionDTO));
                    int position = myPagerAdapter.getPositionById(questionDTO.getId());
                    viewPager.postDelayed(() -> viewPager.setCurrentItem(position, true), 100);
                } else {
                    getActivity().runOnUiThread(() -> myPagerAdapter.removeAll());
                }
            } else if (role.equals("ADMIN")) {
                this.questions = retrieved;
                loadViewPager(view);
            }
        });
        QuestionsMessage message = new QuestionsMessage(MessageType.ALL);
        AnswersApplication.instance().getStompClient().send("/topic/questions", message.json()).subscribe();
    }

    private QuestionDTO findDifference(List<QuestionDTO> listOne, List<QuestionDTO> listTwo) {
        List<Long> idsOne = new ArrayList<>();
        for (QuestionDTO dto : listOne) {
            idsOne.add(dto.getId());
        }
        List<Long> idsTwo = new ArrayList<>();
        for (QuestionDTO dto : listTwo) {
            idsTwo.add(dto.getId());
        }
        idsOne.removeAll(idsTwo);
        if (idsOne.size() > 0) {
            for (QuestionDTO dto : listOne) {
                if (dto.getId().equals(idsOne.get(0))) {
                    return dto;
                }
            }
        }
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_main, container, false);
        this.loadViewPager(view);
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
