package mynote.com.mynote.Database.Local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import mynote.com.mynote.Database.Model.MyCash;
import mynote.com.mynote.Database.Model.MyInMoney;
import mynote.com.mynote.Database.Model.MyOutMoney;
import mynote.com.mynote.Database.Model.Note;
import mynote.com.mynote.Database.Model.User;

@Database(entities = {User.class, Note.class, MyInMoney.class, MyOutMoney.class, MyCash.class},version = 1,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {

    public abstract UserDao userDao();
    public abstract NoteDao noteDao();
    public abstract MyInDao myInDao();
    public abstract MyOutDao myOutDao();
    public abstract MyCashDao myCashDao();


    private static MyDatabase instance;

    public static MyDatabase getInstance(Context context){
        if (instance==null){
            instance= Room.databaseBuilder(context,MyDatabase.class,"mynote")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }


}
