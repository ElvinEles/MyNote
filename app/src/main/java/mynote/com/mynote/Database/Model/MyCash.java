package mynote.com.mynote.Database.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;
import mynote.com.mynote.Common.Common;

@Entity(tableName = "mycash")
public class MyCash {


    @NonNull
    @PrimaryKey()
    @ColumnInfo(name = "mycash_id")
    private int mycash_id;

    @ColumnInfo(name = "mycash_card")
    private String mycash_card;

    @ColumnInfo(name = "mycash_kind")
    private String mycash_kind;

    @ColumnInfo(name = "user_email")
    private String user_email;

    public MyCash() {



    }


    public int getMycash_id() {
        return mycash_id;
    }

    public void setMycash_id(int mycash_id) {
        this.mycash_id = mycash_id;
    }

    public String getMycash_card() {
        return mycash_card;
    }

    public void setMycash_card(String mycash_card) {
        this.mycash_card = mycash_card;
    }

    public String getMycash_kind() {
        return mycash_kind;
    }

    public void setMycash_kind(String mycash_kind) {
        this.mycash_kind = mycash_kind;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    @Override
    public String toString() {
        return "MyCash{" +
                "mycash_id=" + mycash_id +
                ", mycash_card='" + mycash_card + '\'' +
                ", mycash_kind='" + mycash_kind + '\'' +
                ", user_email='" + user_email + '\'' +
                '}';
    }
}
