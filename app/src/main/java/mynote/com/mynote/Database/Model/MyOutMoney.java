package mynote.com.mynote.Database.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "myoutmoney")
public class MyOutMoney {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "money_id")
    private int money_id;

    @ColumnInfo(name = "money_amount")
    private String money_amount;

    @ColumnInfo(name = "money_kind")
    private String money_kind;

    @ColumnInfo(name = "money_date")
    private String money_date;

    @ColumnInfo(name = "money_description")
    private String money_description;

    @ColumnInfo(name = "user_email")
    private String user_email;

    public MyOutMoney() {

    }

    public int getMoney_id() {
        return money_id;
    }

    public void setMoney_id(int money_id) {
        this.money_id = money_id;
    }

    public String getMoney_amount() {
        return money_amount;
    }

    public void setMoney_amount(String money_amount) {
        this.money_amount = money_amount;
    }

    public String getMoney_kind() {
        return money_kind;
    }

    public void setMoney_kind(String money_kind) {
        this.money_kind = money_kind;
    }

    public String getMoney_date() {
        return money_date;
    }

    public void setMoney_date(String money_date) {
        this.money_date = money_date;
    }

    public String getMoney_description() {
        return money_description;
    }

    public void setMoney_description(String money_description) {
        this.money_description = money_description;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }
}
