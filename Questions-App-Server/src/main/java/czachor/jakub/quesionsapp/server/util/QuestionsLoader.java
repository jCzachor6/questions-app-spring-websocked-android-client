package czachor.jakub.quesionsapp.server.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.models.Question;
import czachor.jakub.quesionsapp.server.models.QuestionState;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionsLoader {

    public List<AnswerHolder> load(String path) {
        List<Question> questions = this.loadQuestions(path);
        return questions.stream().map(this::map).collect(Collectors.toList());
    }

    private List<Question> loadQuestions(String path) {
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<List<Question>> typeReference = new TypeReference<List<Question>>() {
        };
        InputStream inputStream = TypeReference.class.getResourceAsStream(path);
        try {
            List<Question> questions = mapper.readValue(inputStream, typeReference);
            System.out.println("Questions loaded");
            return questions;
        } catch (IOException e) {
            System.out.println("Unable to load questions: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public AnswerHolder map(Question question) {
        AnswerHolder holder = new AnswerHolder();
        holder.setQuestion(question);
        holder.setAnswered(new HashMap<>());
        holder.setState(QuestionState.LOCKED);
        return holder;
    }
}
