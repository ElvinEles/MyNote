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
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.List;

import mynote.com.mynote.Adapter.MyoutMoneyAdapter;
import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.MyCash;
import mynote.com.mynote.Database.Model.MyOutMoney;
import mynote.com.mynote.R;


public class MyOutFragment extends Fragment {


    private RecyclerView recyclerView_mymoneyout;
    private MyoutMoneyAdapter adapter;
    private MyDatabase database;
    private List<MyOutMoney> moneyList;
    private FloatingActionButton fab_money_add;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StringBuilder note_date;
    private MaterialEditText money_date;


    public MyOutFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_out, container, false);


        fab_money_add = view.findViewById(R.id.fab_moneyout_note);
        recyclerView_mymoneyout = view.findViewById(R.id.recycler_myoutmoney);
        recyclerView_mymoneyout.setHasFixedSize(true);
        recyclerView_mymoneyout.setLayoutManager(new LinearLayoutManager(getContext()));


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
        builder.setTitle("Məxaric");

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_add_out_money, null, false);

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
        MyOutMoney myoutMoney = new MyOutMoney();
        myoutMoney.setMoney_amount(amount);
        myoutMoney.setMoney_kind(kind);
        if (note_date.equals("") || note_date == null) {
            note_date = new StringBuilder("Zamansız");
        }

        myoutMoney.setMoney_date(note_date.toString());
        myoutMoney.setMoney_description(description);
        myoutMoney.setUser_email(Common.current_user.getUser_email());

        if (kind.equals("Kart")) {


            double firstmoney = Double.valueOf(database.myCashDao().getMycashKart().getMycash_card());
            double newmoney = Double.valueOf(amount);

            if (newmoney > firstmoney) {
                Toast.makeText(getContext(), "Kartda miqdar azdır", Toast.LENGTH_SHORT).show();
                return;
            }


            double lastmoney = lastmoneyCount(firstmoney, newmoney);

            MyCash insertcash = database.myCashDao().getMycashKart();
            insertcash.setMycash_card(String.valueOf(lastmoney));

            database.myCashDao().updateMycashKart(insertcash);


        }


        if (kind.equals("Nağd")) {


            double firstmoney = Double.valueOf(database.myCashDao().getMycashNagd().getMycash_card());
            double newmoney = Double.valueOf(amount);

            if (newmoney > firstmoney) {
                Toast.makeText(getContext(), "Nağd miqdar azdır", Toast.LENGTH_SHORT).show();
                return;
            }

            double lastmoney = lastmoneyCount(firstmoney, newmoney);

            MyCash insertcash = database.myCashDao().getMycashNagd();
            insertcash.setMycash_card(String.valueOf(lastmoney));

            database.myCashDao().updateMycashNagd(insertcash);


        }


        database.myOutDao().insertMyOutMoney(myoutMoney);
        Toast.makeText(getContext(), "Qeyd edildi", Toast.LENGTH_SHORT).show();
        allMoneygets();


    }

    private double lastmoneyCount(double firstmoney, double newmoney) {
        if (firstmoney > newmoney) {
            return firstmoney - newmoney;
        } else if (newmoney == firstmoney || firstmoney == newmoney) {
            return newmoney - firstmoney;
        }

        return 0;

    }

    private void allMoneygets() {
        moneyList = database.myOutDao().getAllOutMoneys(Common.current_user.getUser_email());
        adapter = new MyoutMoneyAdapter(getContext(), moneyList);

        if (adapter != null) {
            recyclerView_mymoneyout.setAdapter(adapter);
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
