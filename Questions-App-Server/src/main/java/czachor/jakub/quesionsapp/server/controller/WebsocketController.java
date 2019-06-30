package czachor.jakub.quesionsapp.server.controller;

import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.models.QuestionState;
import czachor.jakub.quesionsapp.server.models.dto.QuestionDTO;
import czachor.jakub.quesionsapp.server.models.message.QuestionsMessage;
import czachor.jakub.quesionsapp.server.service.QuestionService;
import czachor.jakub.quesionsapp.server.util.Mapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Controller
public class WebsocketController {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final QuestionService questionService;

    public WebsocketController(SimpMessagingTemplate simpMessagingTemplate, QuestionService questionService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.questionService = questionService;
    }

    @MessageMapping("/questions")
    private void message(QuestionsMessage message) {
        System.out.println("Received message: " + message);
        switch (message.getType()) {
            case ALL:
                this.simpMessagingTemplate.convertAndSend("/questions/USER", this.unlockedQuestionDTOs());
                this.simpMessagingTemplate.convertAndSend("/questions/ADMIN", this.questionDTOs());
                break;
            case SINGLE:
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getQuestionId(), getOne(message.getQuestionId()));
                break;
            case UNLOCK:
                this.questionService.unlockById(message.getQuestionId());
                this.simpMessagingTemplate.convertAndSend("/questions/USER", this.unlockedQuestionDTOs());
                this.lockAfterTime(questionService.getById(message.getQuestionId()));
                break;
            case ANSWER:
                this.questionService.addAnswer(message.getQuestionId(), message.getAnswers());
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getQuestionId(), getOne(message.getQuestionId()));
                break;
            case RESULTS:
                this.questionService.unlockToResultsById(message.getQuestionId());
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getQuestionId(), getOne(message.getQuestionId()));
                break;
            case RESET:
                this.questionService.resetAll();
                this.simpMessagingTemplate.convertAndSend("/questions/USER", this.unlockedQuestionDTOs());
                this.simpMessagingTemplate.convertAndSend("/questions/ADMIN", this.questionDTOs());
                break;
        }
    }

    private List<QuestionDTO> questionDTOs() {
        return Mapper.map(this.questionService.getAll());
    }

    private List<QuestionDTO> unlockedQuestionDTOs() {
        return Mapper.map(this.questionService.getUnlocked());
    }

    private QuestionDTO getOne(Long id) {
        return Mapper.map(questionService.getById(id));
    }

    private void lockAfterTime(AnswerHolder answerHolder) {
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            answerHolder.setState(QuestionState.TIME_UP);
            QuestionDTO afterTime = Mapper.map(questionService.getById(answerHolder.getQuestion().getId()));
            this.simpMessagingTemplate.convertAndSend("/questions/" + answerHolder.getQuestion().getId(), afterTime);
        }, answerHolder.getQuestion().getTimeInSeconds(), TimeUnit.SECONDS);
    }
}
