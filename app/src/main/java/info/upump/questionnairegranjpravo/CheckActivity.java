package info.upump.questionnairegranjpravo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairegranjpravo.db.QuestionDAO;
import info.upump.questionnairegranjpravo.entity.Answer;
import info.upump.questionnairegranjpravo.entity.Question;

public class CheckActivity extends AppCompatActivity {

    private TextView goodAnswerText, badAnswerText, questionText;
    private ImageView img;
    private RadioGroup answersGroup;
    private List<Question> questionList = new ArrayList<>();
    private List<Answer> currentAnswerList = new ArrayList<>();
    private int number;
    private int good;
    private int bad;
    private String category;
    private static final String CATEGORY = "cat";


    public static Intent createIntent(Context context, String category) {
        Intent intent = new Intent(context, CheckActivity.class);
        intent.putExtra(CATEGORY, category);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        setTitle(getString(R.string.title_check_acrivity));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        goodAnswerText = findViewById(R.id.check_activity_good_text_view);
        badAnswerText = findViewById(R.id.check_activity_bad_text_view);
        questionText = findViewById(R.id.check_activity_question_text_view);
        img = findViewById(R.id.check_activity_img);
        answersGroup = findViewById(R.id.check_activity_answers_group);

        answersGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                System.out.println(checkedId);
                checkAnswer(checkedId);
            }
        });

        category = getIntent().getStringExtra(CATEGORY);

        initQuestions();
        start();
    }

    private void checkAnswer(int checkedId) {
        Answer answer = currentAnswerList.get(checkedId);
        if (answer.getRight() == 1) {
            goodAnswerText.setText(String.valueOf(++good));
        } else {
            badAnswerText.setText(String.valueOf(++bad));
        }
        if(number < questionList.size()-1){
            start();
        } else startResult();

    }

    private void startResult() {
        final String title = "Ваш результат";
        String message = goodAnswerText.getText() + " правильных ответов";
        String button1String = "Закончить";
        String button2String = "Повторить";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);  // заголовок
        builder.setMessage(message); // сообщение
        builder.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                exit();
            }
        });
        builder.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                reset();
                start();
            }
        });
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void reset() {
        initQuestions();
    }

    private void start() {
        number++;
        Question question = questionList.get(number);
        questionText.setText(question.getBody());
        String s = question.getImg();

        if (s != null) {
            img.setImageResource(getResources().getIdentifier("drawable/" + s, null, getApplication().getApplicationContext().getPackageName()));
            //   img.setLayoutParams(new LinearLayout.LayoutParams(300, 300));
        } else {
            img.setImageDrawable(null);
            // img.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
        }
        currentAnswerList = question.getAnswers();
        answersGroup.removeAllViews();
        int id = 0;
        for (Answer a : currentAnswerList) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(a.getBody());
            radioButton.setPadding(4, 4, 4, 4);
            radioButton.setTextColor(getResources().getColor(R.color.cardview_dark_background));
            radioButton.setId(id);
            answersGroup.addView(radioButton);
            id++;
        }
    }

    private void initQuestions() {
        good = 0;
        bad = 0;
        number = -1;
        badAnswerText.setText(String.valueOf(bad));
        goodAnswerText.setText(String.valueOf(good));
        QuestionDAO questionDAO = new QuestionDAO(this);
        questionList = questionDAO.getQuestions(category);
    }

    private void exit() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
         exit();
        }
        return super.onOptionsItemSelected(item);
    }
}
