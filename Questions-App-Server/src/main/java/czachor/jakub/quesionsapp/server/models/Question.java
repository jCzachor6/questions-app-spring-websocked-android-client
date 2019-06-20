package czachor.jakub.quesionsapp.server.models;

import lombok.Data;

import java.util.List;

@Data
public class Question {
    private Long id;
    private String question;
    private List<String> answers;
    private List<Long> correctAnswers;
    private Long timeInSeconds;
}
