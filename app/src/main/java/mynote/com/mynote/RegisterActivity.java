package mynote.com.mynote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.User;

public class RegisterActivity extends AppCompatActivity {

    private MaterialEditText email;

    private MaterialEditText password;

    private MaterialEditText name;

    private Button button_registr;



    private User user_dba;

    private MyDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database=MyDatabase.getInstance(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        name = findViewById(R.id.name);
        button_registr = findViewById(R.id.button_registr);

        button_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertUserDao();
            }
        });

    }

    private void insertUserDao() {

        final String user_email_edit = email.getText().toString();
        final String user_password_edit = password.getText().toString();
        final String user_name_edit = name.getText().toString();

        if (TextUtils.isEmpty(user_email_edit)) {
            Toast.makeText(this, "Emaili daxil edin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(user_password_edit)) {
            Toast.makeText(this, "Parolu daxil edin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(user_name_edit)) {
            Toast.makeText(this, "Adınızı daxil edin", Toast.LENGTH_SHORT).show();
            return;
        }





            User user_db = new User();
            user_db.setUser_email(user_email_edit);
            user_db.setUser_password(user_password_edit);
            user_db.setUser_name(user_name_edit);

            try {
                database.userDao().registerUser(user_db);
                Toast.makeText(this, user_db.getUser_email() + " qeyd olundu", Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }




            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            finish();
        }


    }




























