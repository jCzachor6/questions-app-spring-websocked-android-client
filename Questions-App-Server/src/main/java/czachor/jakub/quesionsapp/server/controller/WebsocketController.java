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
                this.simpMessagingTemplate.convertAndSend("/questions/", this.questionLookupDTO());
                break;
            case SINGLE:
                QuestionDTO singleDto = Mapper.map(questionService.getById(message.getMessageId()));
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getMessageId(), singleDto);
                break;
            case UNLOCK:
                this.questionService.unlockById(message.getMessageId());
                this.simpMessagingTemplate.convertAndSend("/questions/", this.questionLookupDTO());
                break;
            case ANSWER:
                this.questionService.addAnswer(message.getMessageId(), message.getAnswers());
                QuestionDTO answered = Mapper.map(questionService.getById(message.getMessageId()));
                this.simpMessagingTemplate.convertAndSend("/questions/" + message.getMessageId(), answered);
                break;
            case RESET:
                this.questionService.resetAll();
                this.simpMessagingTemplate.convertAndSend("/questions/", this.questionLookupDTO());
                break;
        }
    }

    private List<QuestionDTO> questionLookupDTO() {
        return Mapper.map(this.questionService.getAll());
    }

}
