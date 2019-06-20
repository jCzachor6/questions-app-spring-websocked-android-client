package czachor.jakub.quesionsapp.server.repository;

import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.util.QuestionsLoader;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class QuestionRepository {
    public List<AnswerHolder> questions = new QuestionsLoader().load("/questions.json");

    public List<AnswerHolder> getAll() {
        return this.questions;
    }

    public Optional<AnswerHolder> getById(Long id) {
        return this.questions.stream().filter(q -> q.getQuestion().getId().equals(id)).findFirst();
    }

    public void reset() {
        this.questions = this.getAll().stream().map(AnswerHolder::getQuestion).map(new QuestionsLoader()::map).collect(Collectors.toList());
    }
}
