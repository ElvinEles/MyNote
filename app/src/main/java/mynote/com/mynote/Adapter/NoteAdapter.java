package mynote.com.mynote.Adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.Note;
import mynote.com.mynote.R;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    Context context;
    List<mynote.com.mynote.Database.Model.Note> noteList;
    private MyDatabase database;

    private StringBuilder note_date;
    private MaterialEditText note_date_edittex;
    private DatePickerDialog.OnDateSetListener onDateSetListener;


    public NoteAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        database = MyDatabase.getInstance(context);
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                note_date = new StringBuilder(String.valueOf(i2)).append((i1 < 10) ? ".0" : ".").append(String.valueOf(i1 + 1)).append(".").append(String.valueOf(i));
                note_date_edittex.setText(note_date);
            }
        };
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View item_view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_item, viewGroup, false);


        return new MyViewHolder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        simpleDateFormat.format(today);

        Log.d("TODAY", simpleDateFormat.format(today));

        if (noteList.get(position).getNote_date().equals(simpleDateFormat.format(today))) {
            myViewHolder.card_date.setTextColor(Color.WHITE);
            myViewHolder.card_date.setBackgroundResource(R.drawable.alert_date);
        }


        myViewHolder.card_date.setText(noteList.get(position).getNote_date());
        myViewHolder.card_note.setText(noteList.get(position).getNote_text());


        myViewHolder.card_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.deleted_note = noteList.get(position);
                database.noteDao().deleteNote(noteList.get(position));
                noteList.remove(position);


                Snackbar snackbar = Snackbar.make(myViewHolder.card_delete, "Silinmiş qeydi geri qaytara bilərsiniz!!!", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.WHITE);


                snackbar.setAction("Geri", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.noteDao().insertNote(Common.deleted_note);
                        noteList.add(Common.deleted_note);


                        notifyDataSetChanged();
                    }

                });
                snackbar.show();

                notifyDataSetChanged();

            }
        });


        myViewHolder.card_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note_up = noteList.get(position);

                showUpdateAlert(note_up);

            }
        });

    }

    private void showUpdateAlert(final Note note_up) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Qeydinizi yeniləyin");

        View view = LayoutInflater.from(context).inflate(R.layout.layout_update_item, null, false);

        note_date_edittex = view.findViewById(R.id.note_date);
        final MaterialEditText note_text = view.findViewById(R.id.note_text);

        note_date_edittex.setText(note_up.getNote_date());
        note_text.setText(note_up.getNote_text());

        note_date_edittex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendatepicker();
            }
        });

        builder.setNegativeButton("Ləğv et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("Təsdiqlə", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

                if (note_date == null) {
                    note_date = new StringBuilder(note_date_edittex.getText());
                }


                noteList.remove(note_up);

                note_up.setNote_date(note_date.toString());
                note_up.setNote_text(note_text.getText().toString());

                database.noteDao().updateNote(note_up);
                noteList.add(note_up);
                notifyDataSetChanged();


            }
        });

        builder.setView(view);
        builder.show();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView card_date;
        public TextView card_note;

        public ImageButton card_delete;
        public ImageButton card_update;


        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);

            card_date = itemView.findViewById(R.id.card_date);
            card_note = itemView.findViewById(R.id.card_note);

            card_delete = itemView.findViewById(R.id.card_delete);
            card_update = itemView.findViewById(R.id.card_update);


        }


    }

    private void opendatepicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(context, onDateSetListener, year, month, day);
        dialog.show();
    }
}
