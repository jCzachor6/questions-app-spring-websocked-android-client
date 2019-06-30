package czachor.jakub.questions.app.fragments;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import czachor.jakub.questions.app.AnswersApplication;
import czachor.jakub.questions.app.R;
import czachor.jakub.questions.app.models.AnswerDto;
import czachor.jakub.questions.app.models.MessageType;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.QuestionState;
import czachor.jakub.questions.app.models.QuestionsMessage;
import czachor.jakub.questions.app.models.sqlite.Answer;
import czachor.jakub.questions.app.models.sqlite.AnswerState;
import czachor.jakub.questions.app.utils.AdminPanelView;
import czachor.jakub.questions.app.utils.AnswersView;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class QuestionFragment extends Fragment {
    private Activity activity;
    private QuestionDTO questionDTO;
    private AnswerDto answerDto;
    private Long timer = 0L;
    private CountDownTimer countDownTimer;
    private TextView questionIdTextView;
    private TextView questionTextView;
    private TextView timerTextView;
    private AnswersView answersView;
    private AdminPanelView adminPanelView;
    private CardView resultsCardView;
    private PieChartView pieChartView;
    private int[] colors = {
            R.color.col1, R.color.col2, R.color.col3, R.color.col4,
            R.color.col5, R.color.col6, R.color.col7, R.color.col8,
            R.color.col9, R.color.col10, R.color.col11, R.color.col12,
            R.color.col13, R.color.col14, R.color.col15
    };


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
        if (AnswersApplication.instance().role().equals("USER")) {
            this.startTimer();
            this.setCheckBoxes();
        } else if (AnswersApplication.instance().role().equals("ADMIN")) {
            this.setCorrectCheckBoxes();
        }
        this.updateCountdownText();
        this.subscribe();
        return view;
    }

    private void subscribe() {
        AnswersApplication.instance().getSubscriptions().addSubscription("/question/" + questionDTO.getId(), topicMessage -> {
            QuestionDTO retrieved = new Gson().fromJson(topicMessage.getPayload(), QuestionDTO.class);
            if (retrieved.getState().equals(QuestionState.SHOW_ANSWERS)) {
                activity.runOnUiThread(() -> {
                    this.resultsCardView.setVisibility(View.VISIBLE);
                    List<SliceValue> pieData = new ArrayList<>();
                    int i = 0;
                    for (Map.Entry<String, Long> entry : retrieved.getAnswered().entrySet()) {
                        pieData.add(new SliceValue(entry.getValue(), colors[i]).setLabel(entry.getKey()));
                        i++;
                    }
                    PieChartData pieChartData = new PieChartData(pieData);
                    pieChartData.setHasLabels(true).setValueLabelTextSize(15);
                    pieChartData.setHasCenterCircle(true)
                            .setCenterText1("Correct: " + AnswersView.AnswerUtils.fromListToString(retrieved.getCorrectAnswers()));
                    pieChartView.setPieChartData(pieChartData);
                });
            }
        });
    }

    private void loadViews(View view) {
        questionIdTextView = view.findViewById(R.id.question_question_id);
        String questionId = "Question #" + questionDTO.getId();
        questionIdTextView.setText(questionId);

        questionTextView = view.findViewById(R.id.question_question);
        questionTextView.setText(questionDTO.getQuestion());

        timerTextView = view.findViewById(R.id.question_question_timer);
        answersView = new AnswersView(view, R.id.question_confirm_button, R.id.answers_layout);
        answersView.initCheckboxes(questionDTO.getAnswers());
        answersView.setOnConfirmButtonClickListener(onConfirmButtonClickListener);
        adminPanelView = new AdminPanelView(view, R.id.unlock_question_button, R.id.show_results_button, R.id.admin_card_view, R.id.reset_all_button);
        adminPanelView.setOnUnlockButtonClickListener(onUnlockButtonClickListener);
        adminPanelView.setOnResultsButtonClickListener(onResultsButtonClickListener);
        adminPanelView.setOnResetAllButtonClickListener(onResetButtonClickListener);

        resultsCardView = view.findViewById(R.id.result_card_view);
        if (AnswersApplication.instance().role().equals("USER")) {
            this.resultsCardView.setVisibility(View.GONE);
        }
        pieChartView = view.findViewById(R.id.chart);
    }

    void loadArgs() {
        Bundle args = getArguments();
        if (args != null) {
            this.questionDTO = (QuestionDTO) args.getSerializable("question");
            this.answerDto = (AnswerDto) args.getSerializable("answer");
            this.timer = questionDTO.getTimeInSeconds() * 1000;
        }
    }

    private void setCheckBoxes() {
        if (answerDto != null && answerDto.getAnswer() != null) {
            this.answersView.checkCheckboxes(answerDto.getAnswer());
            this.answersView.lockAll();
        }
    }

    private void setCorrectCheckBoxes() {
        if (questionDTO != null && questionDTO.getCorrectAnswers() != null) {
            this.answersView.checkCheckboxes(questionDTO.getCorrectAnswers());
            this.answersView.lockAll();
        }
    }

    private void startTimer() {
        if (answerDto.getQuestionId() == null) {
            countDownTimer = new CountDownTimer(this.timer, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    timer = millisUntilFinished;
                    updateCountdownText();
                }

                @Override
                public void onFinish() {
                    if (!answersView.getLocked()) {
                        saveTimeUpAnswer();
                    }
                    answersView.lockAll();
                }
            };
            countDownTimer.start();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.countDownTimer != null) {
            this.countDownTimer.cancel();
        }
        if (answerDto.getQuestionId() != null) {
            this.saveTimeUpAnswer();
        }
    }

    private void saveTimeUpAnswer() {
        AnswerState state = AnswerState.TIME_UP;
        Answer answer = new Answer(
                null,
                questionDTO.getId(),
                "",
                AnswersView.AnswerUtils.fromListToString(questionDTO.getCorrectAnswers()),
                state.toString());
        AnswersApplication.instance().getDaoSession().getAnswerDao().save(answer);
    }

    private View.OnClickListener onConfirmButtonClickListener = v -> {
        this.saveInDatabase();
        this.sendAnswersMessage();
        answersView.lockAll();
    };

    private void saveInDatabase() {
        List<Long> checkedAnswers = answersView.getChecked();
        String answerString = AnswersView.AnswerUtils.fromListToString(checkedAnswers);
        AnswerState state =
                answerString
                        .equals(AnswersView.AnswerUtils.fromListToString(questionDTO.getCorrectAnswers())) ?
                        AnswerState.CORRECT_ANSWER : AnswerState.WRONG_ANSWER;
        Answer answer = new Answer(
                null,
                questionDTO.getId(),
                answerString,
                AnswersView.AnswerUtils.fromListToString(this.questionDTO.getCorrectAnswers()),
                state.toString());
        AnswersApplication.instance().getDaoSession().getAnswerDao().save(answer);
    }

    private View.OnClickListener onUnlockButtonClickListener = v -> {
        this.adminPanelView.lockUnlockButton();
        this.adminPanelView.unlockResultsButton();
        this.sendMessage(MessageType.UNLOCK);
        this.startTimer();
    };

    private View.OnClickListener onResultsButtonClickListener = v -> {
        this.sendMessage(MessageType.RESULTS);
        this.adminPanelView.lockUnlockButton();
        this.adminPanelView.lockResultsButton();
    };

    private View.OnClickListener onResetButtonClickListener = v -> {
        this.sendMessage(MessageType.RESET);
    };

    private void sendMessage(MessageType type) {
        QuestionsMessage message = new QuestionsMessage(type);
        message.setQuestionId(questionDTO.getId());
        AnswersApplication.instance().getStompClient().send("/topic/questions", message.json()).subscribe();
    }

    private void sendAnswersMessage() {
        QuestionsMessage message = new QuestionsMessage(MessageType.ANSWER);
        message.setQuestionId(questionDTO.getId());
        message.setAnswers(this.answersView.getChecked());
        AnswersApplication.instance().getStompClient().send("/topic/questions", message.json()).subscribe();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
