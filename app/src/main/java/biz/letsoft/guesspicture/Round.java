package biz.letsoft.guesspicture;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class Round {
    int roundNum;
    String question;
    String option1;
    String option2;
    String option3;
    String option4;
    String picUrl;
    String correctOption;

    // Конструктор
    Round(String roundNumPar, String option1Par, String option2Par, String option3Par, String option4Par, String picUrlPar, String correctOptionPar, String questionPar)
    {
        this.roundNum = Integer.parseInt(roundNumPar);
        this.option1 = option1Par;
        this.option2 = option2Par;
        this.option3 = option3Par;
        this.option4 = option4Par;
        this.picUrl = picUrlPar;
        this.correctOption = correctOptionPar;
        this.question = questionPar;
    }

    public void startRound(GameActivity gAct, ImageView imageView, TextView textView, Button btn1, Button btn2, Button btn3, Button btn4)
    {
        btn1.setText(option1);
        btn2.setText(option2);
        btn3.setText(option3);
        btn4.setText(option4);
        Glide.with(gAct).load("http://accords.kz/images/android/picture/persons/" + picUrl).into(imageView);
        if(!question.equals(""))
        {
            textView.setText(question);
        }
    }

    public boolean checkAnswer(String userAnsw)
    {
        Boolean res = false;
        String correctOpt = this.correctOption;
        if(correctOpt.equals(userAnsw))
        {
            res = true;
        }
        return res;
    }
}
