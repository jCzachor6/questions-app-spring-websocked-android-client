package czachor.jakub.quesionsapp.server.models.dto;

import czachor.jakub.quesionsapp.server.models.QuestionState;

import java.util.List;
import java.util.Map;

public class QuestionDTO {
    private Long id;
    private String question;
    private List<String> answers;
    private List<Long> correctAnswers;
    private Long timeInSeconds;
    private QuestionState state;
    private Map<List<Long>, Long> answered;
}
