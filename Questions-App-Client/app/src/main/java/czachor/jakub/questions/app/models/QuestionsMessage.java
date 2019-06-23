package czachor.jakub.questions.app.models;

import java.util.List;

public class QuestionsMessage {
    private MessageType type;
    private List<Long> answers;
    private Long messageId;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public List<Long> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Long> answers) {
        this.answers = answers;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
