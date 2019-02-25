package mynote.com.mynote.Fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import mynote.com.mynote.Adapter.NoteAdapter;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.Note;
import mynote.com.mynote.HomeActivity;
import mynote.com.mynote.R;


public class NoteFragment extends Fragment {

    private RecyclerView recyclerView_note;
    private NoteAdapter adapter;
    private MyDatabase database;
    private List<Note> noteList = new ArrayList<>();
    private FloatingActionButton add_note;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StringBuilder note_date;
    private MaterialEditText note_date_edittex;

    private static NoteFragment INSTANCE = null;


    public static NoteFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoteFragment();

        }
        return INSTANCE;

    }


    public NoteFragment() {
        database = MyDatabase.getInstance(getContext());
        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                note_date = new StringBuilder(String.valueOf(i2)).append((i1<10)?".0":".").append(String.valueOf(i1 + 1)).append(".").append(String.valueOf(i));
                note_date_edittex.setText(note_date);
            }
        };


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, container, false);

        recyclerView_note = view.findViewById(R.id.recycler_note);
        recyclerView_note.setHasFixedSize(true);
        recyclerView_note.setLayoutManager(new LinearLayoutManager(getContext()));

        addAllNotesToRecyclerView();

        add_note = view.findViewById(R.id.fab_add_note);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddNoteAlert();
            }
        });


        return view;
    }

    public void addAllNotesToRecyclerView() {
        noteList = database.noteDao().allnotes(Common.current_user.getUser_email());
        adapter = new NoteAdapter(getContext(), noteList);

        if (adapter != null) {
            recyclerView_note.setAdapter(adapter);
        }
    }

    private void showAddNoteAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Qeyd əlavə et!");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_note, null);

        note_date_edittex = view.findViewById(R.id.note_date);
        final MaterialEditText note_text = view.findViewById(R.id.note_text);

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

        builder.setPositiveButton("Qeyd et", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                addNoteToDb(note_text.getText().toString());

            }
        });


        builder.setView(view);
        builder.show();

    }

    private void addNoteToDb(String note_text) {
        mynote.com.mynote.Database.Model.Note note_db = new mynote.com.mynote.Database.Model.Note();
        if (note_date == null) {
            note_date = new StringBuilder("Zamansız");
        }

        note_db.setNote_text(note_text);
        note_db.setNote_date(note_date.toString());
        note_db.setUser_email(Common.current_user.getUser_email());
        database.noteDao().insertNote(note_db);
        Toast.makeText(getContext(), "Qeydiniz əlavə edildi", Toast.LENGTH_SHORT).show();
        addAllNotesToRecyclerView();
    }

    private void opendatepicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getContext(), onDateSetListener, year, month, day);
        dialog.show();
    }


}
