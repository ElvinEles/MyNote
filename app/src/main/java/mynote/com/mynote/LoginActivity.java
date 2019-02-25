package mynote.com.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.User;

public class LoginActivity extends AppCompatActivity {

    private Button button_enter;
    private Button button_register;

    private MaterialEditText email;
    private MaterialEditText password;

    private CheckBox checkBox;

    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        database = MyDatabase.getInstance(this);
        Paper.init(this);

        if (Paper.book().read(Common.KEY_LOGIN) != null && Paper.book().read(Common.KEY_PASSWORD) != null) {

            User user_dp = database.userDao().getCurrentUser(Paper.book().read(Common.KEY_LOGIN).toString());

            if (user_dp != null && user_dp.getUser_password().equals(Paper.book().read(Common.KEY_PASSWORD).toString())) {
                Common.current_user = user_dp;


            }
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));

            finish();
        }

        checkBox = findViewById(R.id.checkbox_remmber);
        button_enter = findViewById(R.id.button_enter);
        button_register = findViewById(R.id.button_registr);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        button_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Emaili daxil edin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Parolu daxil edin", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (checkuser()) {

                    if (checkBox.isChecked()) {
                        Paper.book().write(Common.KEY_LOGIN, email.getText().toString());
                        Paper.book().write(Common.KEY_PASSWORD, password.getText().toString());
                    }


                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Belə user yoxdur", Toast.LENGTH_SHORT).show();
                }


            }
        });

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });

    }

    private boolean checkuser() {
        User user = database.userDao().getCurrentUser(email.getText().toString());

        if (user != null && user.getUser_password().equals(password.getText().toString())) {
            Common.current_user = user;
            return true;
        } else {
            return false;
        }
    }


}





















