package info.upump.questionnairegranjpravo.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;

import java.util.List;
import java.util.concurrent.ExecutionException;

import info.upump.questionnairegranjpravo.entity.Answer;
import info.upump.questionnairegranjpravo.entity.Question;
import info.upump.questionnairegranjpravo.filter.CategoryFilter;
import info.upump.questionnairegranjpravo.model.QuestionViewHolder;
import info.upump.questionnairegranjpravo.task.TaskGetAnswer;
import info.upump.questionnairegranjpravo.R;

/**
 * Created by explo on 23.09.2017.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionViewHolder> {
    private Activity activity;
    protected List<Question> list;
    private CategoryFilter filter;
    private List<Answer> answers;

    public QuestionAdapter(Activity activity, List<Question> list) {
        this.activity = activity;
        this.list = list;
        this.filter = new CategoryFilter(list, this);
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, final int position) {
       holder.bind(list.get(position));
    }

    public void setList(List<Question> list) {
        this.list = list;
    }

    public void filter(String text) {
        filter.filter(text);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
