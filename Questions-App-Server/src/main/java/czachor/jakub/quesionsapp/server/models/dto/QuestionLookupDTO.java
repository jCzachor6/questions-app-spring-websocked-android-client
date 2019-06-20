package czachor.jakub.quesionsapp.server.models.dto;

import czachor.jakub.quesionsapp.server.models.QuestionState;

import java.util.List;
import java.util.Map;

public class QuestionLookupDTO {
    private Long id;
    private String question;
    private List<String> answers;
    private QuestionState state;
}
