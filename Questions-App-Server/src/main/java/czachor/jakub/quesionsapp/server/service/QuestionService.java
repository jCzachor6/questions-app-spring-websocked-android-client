package czachor.jakub.quesionsapp.server.service;

import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.models.QuestionState;
import czachor.jakub.quesionsapp.server.repository.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public List<AnswerHolder> getAll() {
        return questionRepository.getAll();
    }

    public List<AnswerHolder> getUnlocked() {
        return questionRepository.getUnlocked();
    }

    public AnswerHolder getById(Long id) {
        return questionRepository.getById(id).orElse(null);
    }

    public void unlockById(Long id) {
        questionRepository.getById(id).orElse(null).setState(QuestionState.ACTIVE);
    }

    public void unlockToResultsById(Long id) {
        questionRepository.getById(id).orElse(null).setState(QuestionState.SHOW_ANSWERS);
    }

    public void addAnswer(Long messageId, List<Long> answers) {
        AnswerHolder holder = getById(messageId);
        answers.sort(Comparator.comparingLong(Long::longValue));
        holder.getAnswered().merge(answers, 1L, Long::sum);
    }

    public void resetAll() {
        this.questionRepository.reset();
    }
}
