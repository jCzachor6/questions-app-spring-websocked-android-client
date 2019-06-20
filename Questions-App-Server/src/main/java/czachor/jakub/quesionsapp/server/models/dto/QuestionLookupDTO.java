package czachor.jakub.quesionsapp.server.models.dto;

import czachor.jakub.quesionsapp.server.models.QuestionState;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class QuestionLookupDTO {
    private Long id;
    private String question;
    private QuestionState state;
}
