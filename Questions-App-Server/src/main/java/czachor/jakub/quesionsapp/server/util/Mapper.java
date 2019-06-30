package czachor.jakub.quesionsapp.server.util;

import czachor.jakub.quesionsapp.server.models.AnswerHolder;
import czachor.jakub.quesionsapp.server.models.dto.QuestionDTO;
import czachor.jakub.quesionsapp.server.models.dto.QuestionLookupDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Mapper {
    public static List<QuestionDTO> map(List<AnswerHolder> questions) {
        return questions.stream().map(Mapper::map).collect(Collectors.toList());
    }

    public static QuestionDTO map(AnswerHolder answerHolder) {
        QuestionDTO dto = new QuestionDTO();
        dto.setId(answerHolder.getQuestion().getId());
        dto.setQuestion(answerHolder.getQuestion().getQuestion());
        dto.setAnswers(answerHolder.getQuestion().getAnswers());
        dto.setCorrectAnswers(answerHolder.getQuestion().getCorrectAnswers());
        dto.setTimeInSeconds(answerHolder.getQuestion().getTimeInSeconds());
        dto.setState(answerHolder.getState());
        Map<String, Long> map = new HashMap<>();
        for (Map.Entry<List<Long>, Long> entry : answerHolder.getAnswered().entrySet()) {
            map.put(AnswerUtils.fromListToString(entry.getKey()), entry.getValue());
        }
        dto.setAnswered(map);
        return dto;
    }

    public static class AnswerUtils {
        private static String divider = ", ";

        public static List<Long> fromStringToList(String string) {
            List<Long> list = new ArrayList<>();
            String[] afterSplit = string.split(divider);
            for (String cString : afterSplit) {
                if (cString.length() > 0) {
                    char c = cString.charAt(0);
                    list.add((long) (c - 'A'));
                }
            }
            return list;
        }

        public static String fromListToString(List<Long> list) {
            StringBuilder s = new StringBuilder();
            for (Long i : list) {
                char c = (char) (i + 'A');
                s.append(c);
                s.append(divider);
            }
            if (list.size() > 0) {
                s.substring(0, s.length() - divider.length());
            }
            return s.toString();
        }
    }
}
