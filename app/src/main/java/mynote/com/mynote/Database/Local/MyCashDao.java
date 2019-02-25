package mynote.com.mynote.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import mynote.com.mynote.Database.Model.MyCash;

@Dao
public interface MyCashDao {

   /* @Query("UPDATE mycash SET mycash_card =:mycash_card , user_email=:user_email WHERE mycash_id=1")
    void updateMycashKart(String mycash_card, String user_email);*/


   @Update
    void updateMycashKart(MyCash myCash);


    @Update
    void updateMycashNagd(MyCash myCash);


    @Query("SELECT * FROM mycash WHERE mycash_id=1")
    MyCash getMycashKart();

    @Query("SELECT * FROM mycash WHERE mycash_id=2")
    MyCash getMycashNagd();

    @Insert
    void insertCard(MyCash myCash);


}
