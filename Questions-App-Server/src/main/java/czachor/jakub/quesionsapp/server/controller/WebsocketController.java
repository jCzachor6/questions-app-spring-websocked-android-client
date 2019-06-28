package czachor.jakub.quesionsapp.server.controller;

import czachor.jakub.quesionsapp.server.models.dto.QuestionDTO;
import czachor.jakub.quesionsapp.server.models.dto.QuestionLookupDTO;
import czachor.jakub.quesionsapp.server.models.message.QuestionsMessage;
import czachor.jakub.quesionsapp.server.service.QuestionService;
import czachor.jakub.quesionsapp.server.util.Mapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

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
                QuestionDTO singleDto = Mapper.map(questionService.getById(message.getQuestionId()));
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getQuestionId(), singleDto);
                break;
            case UNLOCK:
                this.questionService.unlockById(message.getQuestionId());
                this.simpMessagingTemplate.convertAndSend("/questions/USER", this.unlockedQuestionDTOs());
                break;
            case ANSWER:
                this.questionService.addAnswer(message.getQuestionId(), message.getAnswers());
                QuestionDTO answered = Mapper.map(questionService.getById(message.getQuestionId()));
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getQuestionId(), answered);
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

}
