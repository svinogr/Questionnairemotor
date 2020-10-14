package info.upump.questionnairemotor;

import android.app.Activity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import info.upump.questionnairemotor.db.AnswerDAO;
import info.upump.questionnairemotor.db.QuestionDAO;
import info.upump.questionnairemotor.entity.Answer;
import info.upump.questionnairemotor.entity.Question;

/**
 * Created by explo on 23.09.2017.
 */

public class Reader {
    private Activity activity;
    private List<Question> list;
    private static String alphaRu = new String("абвгдеёжзиыйклмнопрстуфхцчшщьэюя");
    private static String[] alphaEn = {"a", "b", "v", "g", "d", "e", "yo", "g", "z", "i", "y", "i",
            "k", "l", "m", "n", "o", "p", "r", "s", "t", "u",
            "f", "h", "tz", "ch", "sh", "sh", "'", "e", "yu", "ya"};

    public Reader(Activity activity) {
        this.activity = activity;
    }

    public void startReader() throws IOException, JSONException {
        this.list = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(readJsonFile());

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            Question question = new Question();
            question.setBody(jsonObject.get("question").toString());
            String img = (String) jsonObject.get("img");
           // if (!img.equals("имя картинки")) question.setImg(jsonObject.get("img").toString());
            question.setImg(jsonObject.get("img").toString());
            question.setCategory("капитан");
            question.setComment(null);

            JSONArray answers = jsonObject.getJSONArray("answers");
            for (int k = 0; k < answers.length(); k++) {
                Answer answer = new Answer();
                JSONObject jsonObjectAnswer = (JSONObject) answers.get(k);
                answer.setBody(jsonObjectAnswer.get("answer").toString());
                if (jsonObjectAnswer.get("good").toString().equals("true")) {
                    answer.setRight(1);
                } else answer.setRight(-1);
                answer.setQuestion(question);
                question.getAnswers().add(answer);
            }
            list.add(question);
        }
        System.out.println("list size: " + list.size());



    }

    public void writeInDb() {
        QuestionDAO questionDAO = new QuestionDAO(activity.getApplicationContext());
        AnswerDAO answeDAO = new AnswerDAO(activity.getApplicationContext());
        for (Question question : list) {
            int id = (int) questionDAO.save(question);
            question.setId(id);
            for (Answer answer : question.getAnswers()) {
                answer.setQuestion(question);
                answeDAO.save(answer);
            }
        }
    }


    /*
    private String readHtml() {
        String s;
        InputStreamReader scanner = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            scanner = new InputStreamReader(am.open("question.html"), "windows-1251");
            bufferedReader = new BufferedReader(scanner);

            while ((s = bufferedReader.readLine()) != null) {
                stringBuilder.append(s);

            }
            scanner.close();
            bufferedReader.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();

    }
*/
    private String readJsonFile() {
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(activity.getAssets().open("data.json"), "windows-1251"));
            String line;

            while ((line = reader.readLine()) != null) {
                text.append(line);
            }
            System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text.toString();
    }

}
