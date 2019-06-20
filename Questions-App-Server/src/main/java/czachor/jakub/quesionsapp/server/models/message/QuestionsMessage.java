package czachor.jakub.quesionsapp.server.models.message;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class QuestionsMessage {
    private MessageType type;
    private List<Long> answers;
    private Long messageId;
}
