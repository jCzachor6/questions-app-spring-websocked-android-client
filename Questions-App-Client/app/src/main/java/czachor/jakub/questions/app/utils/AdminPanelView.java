package czachor.jakub.questions.app.utils;

import android.view.View;
import android.widget.Button;

import androidx.annotation.IdRes;
import androidx.cardview.widget.CardView;

import czachor.jakub.questions.app.AnswersApplication;

public class AdminPanelView {
    private CardView cardView;
    private View view;
    private Button unlockButton;
    private Button resultsButton;
    private Button resetAllButton;

    public AdminPanelView(View view, @IdRes int unlockButtonId, @IdRes int resultButtonId, @IdRes int cardViewId, @IdRes int resetAllButtonId) {
        this.view = view;
        this.cardView = view.findViewById(cardViewId);
        this.unlockButton = view.findViewById(unlockButtonId);
        this.resultsButton = view.findViewById(resultButtonId);
        this.resetAllButton = view.findViewById(resetAllButtonId);
        this.resultsButton.setEnabled(false);
        if (!AnswersApplication.instance().role().equals("ADMIN")) {
            this.cardView.setVisibility(View.GONE);
        }
    }

    public void setOnUnlockButtonClickListener(View.OnClickListener onUnlockButtonClickListener) {
        this.unlockButton.setOnClickListener(onUnlockButtonClickListener);
    }

    public void setOnResultsButtonClickListener(View.OnClickListener onResultsButtonClickListener) {
        this.resultsButton.setOnClickListener(onResultsButtonClickListener);
    }

    public void setOnResetAllButtonClickListener(View.OnClickListener onResetAllButtonClickListener){
        this.resetAllButton.setOnClickListener(onResetAllButtonClickListener);
    }

    public void lockUnlockButton(){
        this.unlockButton.setEnabled(false);
    }

    public void lockResultsButton(){
        this.resultsButton.setEnabled(false);
    }

    public void unlockResultsButton(){
        this.resultsButton.setEnabled(true);
    }
}
