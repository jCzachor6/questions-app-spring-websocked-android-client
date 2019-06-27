package czachor.jakub.questions.app.models.sqlite;

public enum AnswerState {
    TIME_UP,
    CORRECT_ANSWER,
    WRONG_ANSWER;

    public static AnswerState fromName(String name) {
        for (AnswerState state : AnswerState.values()) {
            if (state.toString().equals(name)) {
                return state;
            }
        }
        return null;
    }
}
