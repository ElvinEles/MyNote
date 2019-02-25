package mynote.com.mynote.Database.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "note")
public class Note {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    private int note_id;
    @ColumnInfo(name = "note_text")
    private String note_text;
    @ColumnInfo(name = "user_email")
    private String user_email;
    @ColumnInfo(name = "note_date")
    private String note_date;


    public Note() {
    }



    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_text() {
        return note_text;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }
}

