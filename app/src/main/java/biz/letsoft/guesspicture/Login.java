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

public class Login extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        EditText login_edit_text = (EditText) findViewById(R.id.login_edit_text);
        EditText password_edit_text = (EditText) findViewById(R.id.password_edit_text);
        login_edit_text.setText("axmetovia");
        password_edit_text.setText("Alyusha321");
    }

    public void check_user(View View)
    {
        EditText login_edit_text = (EditText) findViewById(R.id.login_edit_text);
        EditText password_edit_text = (EditText) findViewById(R.id.password_edit_text);
        String login = login_edit_text.getText().toString();
        String password = password_edit_text.getText().toString();
        user_login(login, password);
    }

    public void user_login(String login, String password)
    {
        Boolean res = false;
        new PostClass(this).execute(login, password);
    }

    public void create_User(String login, String password)
    {
        User user_one = new User(login, password);
        DBHelper dbHelper = new DBHelper(this);
    }

    public void registration(View view)
    {
        Intent intent = new Intent(Login.this, Registration.class);
        startActivity(intent);
    }

    private class PostClass extends AsyncTask<String, Void, Void> {
        private final Context context;

        public PostClass(Context c){

            this.context = c;
        }

        @Override
        protected Void doInBackground(String... params) {
            String login = params[0];
            String password = params[1];
            try {
                final StringBuilder output = new StringBuilder();

                URL url = new URL("http://accords.kz/android.php");
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                //присваеваем элементу массива MESSAGE текст сообщения
                String urlParameters = "LOGIN="+login+"&PASSWORD="+password;
                connection.setRequestMethod("POST");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "ru-RU,ru;0.5");
                connection.setRequestProperty("charset", "utf-8");
                connection.setDoOutput(true);
                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(urlParameters);
                dStream.flush();
                dStream.close();
                int responseCode = connection.getResponseCode();

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

                Login.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //Если вернул елиницу, выводим активити чат, если нет, нехуй пиздеть, пусть заново вбивает
                        if(output_str.equals("1")) {
                            Intent intent = new Intent(Login.this, GameActivity.class);
                            startActivity(intent);
                            create_User("login_test", "pass_test");
                        }else{
                            Toast.makeText(Login.this, "Неправильный пароль или логин", Toast.LENGTH_SHORT).show();
                        }
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

        protected void onPostExecute()
        {

        }

    }
}