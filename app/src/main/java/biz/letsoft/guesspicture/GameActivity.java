package biz.letsoft.guesspicture;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity
{
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> qId_list = new ArrayList<String>();
    String correctAnsw;
    int chapterNum = 1;
    int roundNumInt = 0;
    String user_id = "1";
    boolean finished = false;
    AlertDialog.Builder ad;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();
        game_start();
    }

    public void onMyButtonClick(View view)
    {
        Button b = (Button)view;
        String buttonText = b.getText().toString();
        checkAnswer(buttonText);
    }

    //метод проверяет ответ пользователя
    public void checkAnswer(String answer)
    {
        if(answer.equals(this.correctAnsw)){
            //Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show();
            changeToNextQuestion();
        }
            else
        {
            //Toast.makeText(this, "Неправильно...", Toast.LENGTH_SHORT).show();
            changeToNextQuestion();
        }
    }

    public void changeToNextQuestion()
    {
        game_start();
    }

    public void game_start()
    {
        Toast.makeText(this, "qId_list "+qId_list, Toast.LENGTH_SHORT).show();
        if(finished == true)
        {
            roundFinishAlertShow();
            return;
        }
        //если массив туров пустой
        if (qId_list.isEmpty())
        {
            //и если тур не закончен раунд равен нулю
            if(finished != true)
            {
                this.roundNumInt = 0;
            }
        }
            else
            //если массив туров не пустой раунд равен первому элементу массива
        {
            //присваиваем раунду значение первого элемента массива переводя из стринга в инт
            this.roundNumInt = Integer.parseInt(qId_list.iterator().next());

            //после присвоения, удаляем первый элемент массива
            qId_list.remove(0);
        }

        if(!finished)
        {
            //отправляем пост запрос со значением тура и раунда
            new PostClass(this).execute("" + roundNumInt, "" + chapterNum);
        }

        //если из массива удалился последний элемент обозначаем тур законченным
        if (qId_list.isEmpty() && roundNumInt != 0)
        {
            this.finished = true;
        }
    }

    public void roundFinishAlertShow()
    {
        context = GameActivity.this;
        ad = new AlertDialog.Builder(context);
        ad.setTitle("Тест завершен");  // заголовок
        ad.setMessage("Вы набрали "+"очков"); // сообщение
        ad.setPositiveButton("Повторить тест", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                replayRound();
            }
        });
        ad.setNegativeButton("Следующий тур", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                changeRound();
            }
        });
        ad.show();
    }

    public void replayRound()
    {
        //чистим массив qId_list
        qId_list.clear();
        this.finished = false;
        this.roundNumInt = 0;
        game_start();
    }

    public void changeRound()
    {
        //чистим массив qId_list
        qId_list.clear();
        this.finished = false;
        this.chapterNum++;
        this.roundNumInt = 0;
        game_start();
    }

    public void buildGraph(String[] all_q)
    {
        //чистим массив с раундами
        list.clear();
        //добавляем в массив данные раунда
        for(int i = 0; i < 9; i++)
        {
            this.list.add(all_q[i]);
        }

        //если массив с айди раундов пустой - добавляем данные
        if (qId_list.isEmpty())
        {
            for (int i = 9; i < all_q.length; i++)
            {
                this.qId_list.add(all_q[i]);
            }
        }
        buildGraphWithArray();
    }

    //метод строит графику раунда
    public void buildGraphWithArray()
    {
        String id = list.get(1);
        String option1Par = list.get(3);
        String option2Par = list.get(4);
        String option3Par = list.get(5);
        String option4Par = list.get(6);
        String picUrlPar = list.get(2);
        String correctOptionPar = list.get(7);
        String questionPar = list.get(8);
        Round roundObj = new Round(id, option1Par, option2Par, option3Par, option4Par, picUrlPar, correctOptionPar, questionPar);
        this.correctAnsw = correctOptionPar;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        TextView textView = (TextView) findViewById(R.id.textView);
        Button btn1 = (Button) findViewById(R.id.imageButton);
        Button btn2 = (Button) findViewById(R.id.imageButton2);
        Button btn3 = (Button) findViewById(R.id.imageButton3);
        Button btn4 = (Button) findViewById(R.id.imageButton4);
        roundObj.startRound(GameActivity.this, imageView, textView, btn1, btn2, btn3, btn4);
    }

    private class PostClass extends AsyncTask<String, Void, Void> {
        private ProgressDialog progress;
        private final Context context;

        public PostClass(Context c){

            this.context = c;
        }

        protected void onPreExecute(){
            progress= new ProgressDialog(this.context);
            progress.setMessage("Loading");
            progress.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String roundNum = params[0];
            String chapterNum = params[1];
            try {
                final StringBuilder output = new StringBuilder();

                URL url = new URL("http://accords.kz/guess.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                //присваеваем элементам массива данные о раунде, туре и айди юзера
                String urlParameters = "ROUND_NUM="+roundNum+"&CHAPTER="+chapterNum+"&USER_ID="+user_id;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "ru-RU,ru;0.5");
                connection.setRequestProperty("charset","utf-8");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuilder responseOutput = new StringBuilder();
                //пока буферед ридер не равен пустому значению
                while((line = br.readLine()) != null )
                {
                    //добавляем строки лайн в стринг билдер
                    responseOutput.append(line);
                }
                br.close();
                //добавляем один стринг билдер в другой с финал
                output.append(responseOutput.toString());

                GameActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //приводим стринг билдер к обычному стрингу
                        String strList = ""+output;
                        //сплитом добавляем в строковый массив деля символами '//'
                        String[] array = strList.split("//");

                        buildGraph(array);
                        progress.dismiss();
                    }
                });


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute() {
            progress.dismiss();
        }

    }

}
