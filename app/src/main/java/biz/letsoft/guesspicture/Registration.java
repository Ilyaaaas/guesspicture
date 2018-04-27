package biz.letsoft.guesspicture;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Registration extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();
    }

    public void lets_reg(View view)
    {
        EditText editText_name = (EditText) findViewById(R.id.reg_name);
        EditText editText_sername = (EditText) findViewById(R.id.reg_sername);
        EditText editText_login = (EditText) findViewById(R.id.reg_login);
        EditText editText_pass = (EditText) findViewById(R.id.reg_password);
        EditText editText_pass2 = (EditText) findViewById(R.id.reg_password2);

        String name = editText_name.getText().toString().replaceAll(" ", "");
        String sername = editText_sername.getText().toString().replaceAll(" ", "");
        String login = editText_login.getText().toString().replaceAll(" ", "");
        String pass = editText_pass.getText().toString().replaceAll(" ", "");
        String pass2 = editText_pass2.getText().toString().replaceAll(" ", "");

        if(!name.equals("") & !sername.equals("") & !login.equals("") & !pass.equals(""))
        {
            if(pass.equals(pass2)) {
                new PostClass(this).execute(name, sername, login, pass);
                regs_right(name, sername, login, pass);
            }
                else
            {
                Toast.makeText(Registration.this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                editText_pass.requestFocus();
            }
        }
            else
        {
            Toast.makeText(Registration.this, "Не все поля заполнены!", Toast.LENGTH_SHORT).show();
        }
    }

    public void regs_right(String name, String sername, String login, String pass)
    {
        change_Activity();
    }

    public void change_Activity()
    {
        Intent intent = new Intent(Registration.this, GameActivity.class);
        startActivity(intent);
    }

    private class PostClass extends AsyncTask<String, Void, Void>
    {
        private final Context context;

        public PostClass(Context c){

            this.context = c;
        }

        @Override
        protected Void doInBackground(String... params) {
            String name = params[0];
            String sername = params[1];
            String login = params[2];
            String password = params[3];
            try {
                final StringBuilder output = new StringBuilder();

                URL url = new URL("http://accords.kz/android.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                //присваеваем элементу массива MESSAGE текст сообщения
                String urlParameters = "REG_LOGIN="+login+"&REG_PASSWORD="+password+"&REG_NAME="+name+"&REG_SERNAME="+sername;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "ru-RU,ru;0.5");
                connection.setRequestProperty("charset", "utf-8");
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
                final String output_str = output+"";

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

    }
}

