package czachor.jakub.questions.app.utils;

/**
 * @author jakub
 * Created on 23.08.2019.
 */
public class StringUtils {
    public static String defaultString(String str, String defaultString){
        if(str != null){
            return str;
        }
        return defaultString;
    }
}
