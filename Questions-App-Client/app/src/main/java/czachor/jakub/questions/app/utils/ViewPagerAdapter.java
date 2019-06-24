package czachor.jakub.questions.app.utils;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

import czachor.jakub.questions.app.AnswersApplication;
import czachor.jakub.questions.app.fragments.QuestionFragment;
import czachor.jakub.questions.app.models.QuestionDTO;
import czachor.jakub.questions.app.models.sqlite.Answer;
import czachor.jakub.questions.app.models.sqlite.AnswerDao;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<QuestionDTO> questions;

    public ViewPagerAdapter(FragmentManager fm, List<QuestionDTO> questions) {
        super(fm);
        this.questions = questions;
    }

    @Override
    public Fragment getItem(int position) {
        QuestionDTO q = questions.get(position);
        Answer answer = getAnswerById(q.getId());
        return QuestionFragment.newInstance(questions.get(position), Mapper.map(answer));
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "#" + questions.get(position).getId();
    }

    private Answer getAnswerById(Long id) {
        return AnswersApplication
                .instance()
                .getDaoSession()
                .getAnswerDao()
                .queryBuilder()
                .where(AnswerDao.Properties.QuestionId.eq(id))
                .unique();
    }
}
