package czachor.jakub.questions.app.utils;


import androidx.annotation.NonNull;
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
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
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
                .limit(1)
                .unique();
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void addElement(QuestionDTO dto) {
        this.questions.add(dto);
        this.notifyDataSetChanged();
    }

    public void removeAll() {
        this.questions.clear();
        this.notifyDataSetChanged();
    }

    public int getPositionById(Long id) {
        for (int i = 0; i < this.questions.size(); i++) {
            if (this.questions.get(i).getId().equals(id)) {
                return i;
            }
        }
        return questions.size();
    }
}
