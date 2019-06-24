package czachor.jakub.questions.app.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.AnswerDto;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.sqlite.Answer;

public class QuestionFragment extends Fragment {
    private QuestionDTO questionDTO;
    private AnswerDto answerDto;
    private Long timer = 0L;
    private CountDownTimer countDownTimer;
    private TextView questionIdTextView;
    private TextView questionTextView;
    private TextView timerTextView;
    private Button confirmButton;
    private LinearLayout answersLayout;
    private List<CheckBox> answersCheckboxList = new ArrayList<>();

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
        this.loadViews(view);
        return view;
    }

    private void loadViews(View view) {
        questionIdTextView = view.findViewById(R.id.question_question_id);
        String questionId = "Question #" + questionDTO.getId();
        questionIdTextView.setText(questionId);

        questionTextView = view.findViewById(R.id.question_question);
        questionTextView.setText(questionDTO.getQuestion());

        timerTextView = view.findViewById(R.id.question_question_timer);
        confirmButton = view.findViewById(R.id.question_confirm_button);
        answersLayout = view.findViewById(R.id.answers_layout);
        for (String answer : this.questionDTO.getAnswers()) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(answer);
            answersCheckboxList.add(checkBox);
            answersLayout.addView(checkBox);
        }
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.questionDTO = (QuestionDTO) args.getSerializable("question");
            this.answerDto = (AnswerDto) args.getSerializable("answer");
            this.timer = questionDTO.getTimeInSeconds() * 1000;
            this.startTimer();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(this.timer, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                disableQuestion();
            }
        };
        countDownTimer.start();
    }

    private void disableQuestion() {
        this.confirmButton.setEnabled(false);
        for (CheckBox checkBox : this.answersCheckboxList) {
            checkBox.setEnabled(false);
        }
    }

    private void updateCountdownText() {
        String minutes = String.valueOf(this.timer / 60_000);
        String seconds = String.valueOf(this.timer % 60_000 / 1000);
        minutes = minutes.length() == 1 ? "0" + minutes : minutes;
        seconds = seconds.length() == 1 ? "0" + seconds : seconds;

        String timeFormatted = minutes + ":" + seconds;
        timerTextView.setText(timeFormatted);
    }
}
