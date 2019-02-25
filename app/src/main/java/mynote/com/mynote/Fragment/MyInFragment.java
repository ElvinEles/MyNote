package mynote.com.mynote.Fragment;


import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

import mynote.com.mynote.Adapter.MyInMoneyAdapter;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.MyCash;
import mynote.com.mynote.Database.Model.MyInMoney;
import mynote.com.mynote.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInFragment extends Fragment {


    private RecyclerView recyclerView_mymoneyin;
    private MyInMoneyAdapter adapter;
    private MyDatabase database;
    private List<MyInMoney> moneyList;
    private FloatingActionButton fab_money_add;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StringBuilder note_date;
    private MaterialEditText money_date;


    public MyInFragment() {
        database = MyDatabase.getInstance(getContext());

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                note_date = new StringBuilder(String.valueOf(i2)).append((i1 < 10) ? ".0" : ".").append(String.valueOf(i1 + 1)).append(".").append(String.valueOf(i));
                money_date.setText(note_date);
            }
        };
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_in, container, false);


        fab_money_add = view.findViewById(R.id.fab_moneyin_note);
        recyclerView_mymoneyin = view.findViewById(R.id.recycler_myinmoney);
        recyclerView_mymoneyin.setHasFixedSize(true);
        recyclerView_mymoneyin.setLayoutManager(new LinearLayoutManager(getContext()));


        allMoneygets();

        fab_money_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddMoneyAlert();
            }
        });


        return view;
    }

    private void showAddMoneyAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Mədaxil");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_money_item, null, false);

        money_date = view.findViewById(R.id.money_date);
        final MaterialEditText money_amount = view.findViewById(R.id.money_amount);
        final MaterialEditText money_description = view.findViewById(R.id.money_description);
        final Spinner money_kind = view.findViewById(R.id.money_kind);

        money_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendatepicker();
            }
        });


        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(getContext(), R.array.kind, android.R.layout.simple_spinner_item);
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        money_kind.setAdapter(list);


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

                addMoneyToDb(money_amount.getText().toString(), money_description.getText().toString(), money_kind.getSelectedItem().toString());

            }
        });


        builder.setView(view);
        builder.show();
    }

    private void addMoneyToDb(String amount, String description, String kind) {


        MyInMoney myInMoney = new MyInMoney();
        myInMoney.setMoney_amount(amount);
        myInMoney.setMoney_kind(kind);
        if (note_date.equals("") || note_date == null) {
            note_date = new StringBuilder("Zamansız");
        }

        myInMoney.setMoney_date(note_date.toString());
        myInMoney.setMoney_description(description);
        myInMoney.setUser_email(Common.current_user.getUser_email());

        database.myInDao().insertMyInMoney(myInMoney);

        if (kind.equals("Kart")) {


            double firstmoney = Double.valueOf(database.myCashDao().getMycashKart().getMycash_card());
            double newmoney = Double.valueOf(amount);

            double lastmoney = firstmoney + newmoney;

            MyCash insertcash=database.myCashDao().getMycashKart();
            insertcash.setMycash_card(String.valueOf(lastmoney));

            database.myCashDao().updateMycashKart(insertcash);



        }


        if (kind.equals("Nağd")) {


            double firstmoney = Double.valueOf(database.myCashDao().getMycashNagd().getMycash_card());
            double newmoney = Double.valueOf(amount);

            double lastmoney = firstmoney + newmoney;

            MyCash insertcash=database.myCashDao().getMycashNagd();
            insertcash.setMycash_card(String.valueOf(lastmoney));

            database.myCashDao().updateMycashNagd(insertcash);



        }

        Toast.makeText(getContext(), "Qeyd edildi", Toast.LENGTH_SHORT).show();
        allMoneygets();


    }

    private void allMoneygets() {
        moneyList = database.myInDao().getAllMoneys(Common.current_user.getUser_email());
        adapter = new MyInMoneyAdapter(getContext(), moneyList);

        if (adapter != null) {
            recyclerView_mymoneyin.setAdapter(adapter);
        }
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
