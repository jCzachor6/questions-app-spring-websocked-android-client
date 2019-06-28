package czachor.jakub.questions.app.utils;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;

import java.util.ArrayList;
import java.util.List;

import czachor.jakub.questions.app.R;

public class AnswersView {
    private LinearLayout answersLayout;
    private List<CheckBox> answersCheckboxList = new ArrayList<>();
    private View view;
    private Button confirmButton;
    private Boolean isLocked = false;

    public AnswersView(View view, @IdRes int buttonId, @IdRes int linearLayoutId) {
        this.view = view;
        confirmButton = view.findViewById(R.id.question_confirm_button);
        answersLayout = view.findViewById(R.id.answers_layout);
    }

    public void initCheckboxes(List<String> answers) {
        char c = 'A';
        for (String answer : answers) {
            CheckBox checkBox = new CheckBox(view.getContext());
            String ans = c + ". " + answer;
            checkBox.setText(ans);
            answersCheckboxList.add(checkBox);
            answersLayout.addView(checkBox);
            c++;
        }
    }

    public void lockAll() {
        this.confirmButton.setEnabled(false);
        for (CheckBox checkBox : this.answersCheckboxList) {
            checkBox.setEnabled(false);
        }
        this.isLocked = true;
    }

    public void checkCheckboxes(String answer) {
        List<Long> list = AnswerUtils.fromStringToList(answer);
        this.checkCheckboxes(list);
    }

    public void checkCheckboxes(List<Long> answers) {
        for (Long ans : answers) {
            this.answersCheckboxList.get(ans.intValue()).setChecked(true);
        }
    }

    public List<Long> getChecked() {
        List<Long> checked = new ArrayList<>();
        for (int i = 0; i < this.answersCheckboxList.size(); i++) {
            if (this.answersCheckboxList.get(i).isChecked()) {
                checked.add((long) i);
            }
        }
        return checked;
    }

    public static class AnswerUtils {
        private static String divider = ", ";

        public static List<Long> fromStringToList(String string) {
            List<Long> list = new ArrayList<>();
            String[] afterSplit = string.split(divider);
            for (String cString : afterSplit) {
                if (cString.length() > 0) {
                    char c = cString.charAt(0);
                    list.add((long) (c - 'A'));
                }
            }
            return list;
        }

        public static String fromListToString(List<Long> list) {
            StringBuilder s = new StringBuilder();
            for (Long i : list) {
                char c = (char) (i + 'A');
                s.append(c);
                s.append(divider);
            }
            if (list.size() > 0) {
                s.substring(0, s.length() - divider.length());
            }
            return s.toString();
        }
    }

    public void setOnConfirmButtonClickListener(View.OnClickListener onConfirmButtonClickListener) {
        this.confirmButton.setOnClickListener(onConfirmButtonClickListener);
    }

    public Boolean getLocked() {
        return isLocked;
    }
}
