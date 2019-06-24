package czachor.jakub.questions.app;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import czachor.jakub.questions.app.models.sqlite.DaoMaster;
import czachor.jakub.questions.app.models.sqlite.DaoSession;
import czachor.jakub.questions.app.models.sqlite.User;
import czachor.jakub.questions.app.models.sqlite.UserDao;
import czachor.jakub.questions.app.utils.Subscriptions;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class AnswersApplication extends Application {
    private static AnswersApplication application;
    private DaoSession daoSession;
    private User user;
    private StompClient stompClient;
    private Subscriptions subscriptions;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.initDatabase();
        this.loadUser();
        this.initWebsocket();
    }

    private void initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "answers-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public void initWebsocket() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://10.0.2.2:8080/ws/websocket");
        stompClient.connect();
        subscriptions = new Subscriptions();
    }

    private void loadUser() {
        UserDao userDao = daoSession.getUserDao();
        user = userDao.load(1L);
        if (user == null) {
            user = new User();
            user.setRole("USER");
            userDao.save(user);
        }
    }

    public static AnswersApplication instance() {
        return application;
    }

    public String role() {
        return user.getRole();
    }

    public StompClient getStompClient() {
        return stompClient;
    }

    public Subscriptions getSubscriptions() {
        return subscriptions;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
