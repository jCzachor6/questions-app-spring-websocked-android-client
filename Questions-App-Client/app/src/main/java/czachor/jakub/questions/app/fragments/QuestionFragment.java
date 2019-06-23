package czachor.jakub.questions.app.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.AnswerDto;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.sqlite.Answer;

public class QuestionFragment extends Fragment {
    private QuestionDTO questionDTO;
    private AnswerDto answerDto;

    public static QuestionFragment newInstance(QuestionDTO questionDTO, AnswerDto answerDto) {
        QuestionFragment f = new QuestionFragment();
        Bundle args = new Bundle();
        args.putSerializable("question", questionDTO);
        args.putSerializable("answer", answerDto);
        f.setArguments(args);
        return f;
    }


    public QuestionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        this.loadArgs();

        return view;
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.questionDTO = (QuestionDTO) args.getSerializable("question");
            this.answerDto = (AnswerDto) args.getSerializable("answer");
        }
    }
}
