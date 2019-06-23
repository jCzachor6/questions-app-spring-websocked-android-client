package czachor.jakub.questions.app;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import czachor.jakub.questions.app.models.sqlite.DaoMaster;
import czachor.jakub.questions.app.models.sqlite.DaoSession;
import czachor.jakub.questions.app.models.sqlite.User;
import czachor.jakub.questions.app.models.sqlite.UserDao;

public class AnswersApplication extends Application {
    private static AnswersApplication application;
    private DaoSession daoSession;
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.initDatabase();
        this.loadUser();
    }

    private void initDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "answers-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
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
}
