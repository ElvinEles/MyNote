package mynote.com.mynote.Model;

public class Note {

    private int note_id;

    private String note_text;

    private String user_email;

    private String note_date;


    public Note() {
    }

    public Note(String note_text, String user_email, String note_date) {
        this.note_text = note_text;
        this.user_email = user_email;
        this.note_date = note_date;
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
