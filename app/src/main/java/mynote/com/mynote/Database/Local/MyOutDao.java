package mynote.com.mynote.Database.Local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mynote.com.mynote.Database.Model.MyInMoney;
import mynote.com.mynote.Database.Model.MyOutMoney;

@Dao
public interface MyOutDao {


    @Insert
    void insertMyOutMoney(MyOutMoney myOutMoney);

    @Delete
    void deleteMyOutMoney(MyOutMoney myOutMoney);

    @Update
    void updateMyOutMoney(MyOutMoney myOutMoney);

    @Query("SELECT * FROM myoutmoney WHERE user_email=:user_email ORDER BY money_date")
    List<MyOutMoney> getAllOutMoneys(String user_email);
}
