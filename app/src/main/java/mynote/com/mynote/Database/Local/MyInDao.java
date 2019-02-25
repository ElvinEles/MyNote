package mynote.com.mynote.Database.Local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import mynote.com.mynote.Database.Model.MyInMoney;

@Dao
public interface MyInDao {

    @Insert
    void insertMyInMoney(MyInMoney myInMoney);

    @Delete
    void deleteMyInMoney(MyInMoney myInMoney);

    @Update
    void updateMyInMoney(MyInMoney myInMoney);

    @Query("SELECT * FROM myinmoney WHERE user_email=:user_email ORDER BY money_date")
    List<MyInMoney> getAllMoneys(String user_email);

}
