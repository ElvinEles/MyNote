package mynote.com.mynote.Database.Local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import mynote.com.mynote.Database.Model.User;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user WHERE user_email=:user_email")
    User getCurrentUser(String user_email);

    @Insert
    void registerUser(User...users);


}
