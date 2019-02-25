package mynote.com.mynote.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.List;

import mynote.com.mynote.Common.Common;
import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.Database.Model.MyCash;
import mynote.com.mynote.Database.Model.MyInMoney;
import mynote.com.mynote.R;

public class MyInMoneyAdapter extends RecyclerView.Adapter<MyInMoneyAdapter.MyViewHolder> {

    private Context context;
    private List<MyInMoney> moneyList;
    private MyDatabase database;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StringBuilder note_date;
    private MaterialEditText money_date;


    public MyInMoneyAdapter(Context context, List<MyInMoney> moneyList) {
        this.context = context;
        this.moneyList = moneyList;
        database = MyDatabase.getInstance(context);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                note_date = new StringBuilder(String.valueOf(i2)).append((i1 < 10) ? ".0" : ".").append(String.valueOf(i1 + 1)).append(".").append(String.valueOf(i));
                money_date.setText(note_date);
            }
        };

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_myinmoney_item, viewGroup, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {

        myViewHolder.money_date.setText(moneyList.get(position).getMoney_date());
        myViewHolder.money_amount.setText(new StringBuilder(moneyList.get(position).getMoney_amount()).append(" AZN"));
        myViewHolder.money_kind.setText(moneyList.get(position).getMoney_kind());
        myViewHolder.money_description.setText(moneyList.get(position).getMoney_description());

        myViewHolder.money_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyInMoney myInMoney = moneyList.get(position);
                showUpdateBuilder(myInMoney);

            }
        });

        myViewHolder.money_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.deleted_myinmoney = moneyList.get(position);
                database.myInDao().deleteMyInMoney(moneyList.get(position));
                moneyList.remove(position);

                Snackbar snackbar = Snackbar.make(myViewHolder.money_delete, "Silinmiş qeydi geri qaytara bilərsiniz!!!", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.WHITE);


                snackbar.setAction("Geri", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.myInDao().insertMyInMoney(Common.deleted_myinmoney);
                        moneyList.add(Common.deleted_myinmoney);
                        notifyDataSetChanged();
                    }

                });
                snackbar.show();

                notifyDataSetChanged();
            }
        });
    }

    private void showUpdateBuilder(final MyInMoney myInMoney) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Yeniləyin");

        View view = LayoutInflater.from(context).inflate(R.layout.layout_update_myinmoney_item, null, false);

        money_date = view.findViewById(R.id.money_date);
        final MaterialEditText money_amount = view.findViewById(R.id.money_amount);
        MaterialEditText money_description = view.findViewById(R.id.money_description);

        final Spinner spinner_update = view.findViewById(R.id.money_kind);

        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.kind, android.R.layout.simple_spinner_item);
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_update.setAdapter(list);


        money_date.setText(myInMoney.getMoney_date());
        money_amount.setText(myInMoney.getMoney_amount());
        spinner_update.setSelection(list.getPosition(myInMoney.getMoney_kind()));
        money_description.setText(myInMoney.getMoney_description());


        final Double first_money_text = Double.valueOf(myInMoney.getMoney_amount());


        money_date.setOnClickListener(new View.OnClickListener() {
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

                String second_money_kind_text = spinner_update.getSelectedItem().toString();
                Double database_money_card = Double.valueOf(database.myCashDao().getMycashKart().getMycash_card());
                Double database_money_nagd = Double.valueOf(database.myCashDao().getMycashNagd().getMycash_card());
                MyCash myCash_card = database.myCashDao().getMycashKart();
                MyCash myCash_nagd = database.myCashDao().getMycashNagd();

                if (note_date == null) {
                    note_date = new StringBuilder(money_date.getText());
                }


                if (myInMoney.getMoney_kind().equals("Kart")) {

                    if (second_money_kind_text.equals("Nağd")) {
                        moneyList.remove(myInMoney);
                        myInMoney.setMoney_date(note_date.toString());
                        myInMoney.setMoney_description(myInMoney.getMoney_description());
                        myInMoney.setMoney_amount(money_amount.getText().toString());
                        myInMoney.setMoney_kind(spinner_update.getSelectedItem().toString());
                        myInMoney.setUser_email(Common.current_user.getUser_email());


                        myCash_nagd.setMycash_card(String.valueOf(database_money_nagd + Double.valueOf(money_amount.getText().toString())));
                        myCash_card.setMycash_card(String.valueOf(database_money_card - Double.valueOf(money_amount.getText().toString())));

                        database.myCashDao().updateMycashNagd(myCash_nagd);
                        database.myCashDao().updateMycashKart(myCash_card);
                        database.myInDao().updateMyInMoney(myInMoney);

                        moneyList.add(myInMoney);
                        notifyDataSetChanged();


                        Toast.makeText(context, "Qeyd edildi", Toast.LENGTH_SHORT).show();

                    } else {
                        moneyList.remove(myInMoney);
                        myInMoney.setMoney_date(note_date.toString());
                        myInMoney.setMoney_description(myInMoney.getMoney_description());
                        myInMoney.setMoney_amount(money_amount.getText().toString());
                        myInMoney.setMoney_kind(spinner_update.getSelectedItem().toString());
                        myInMoney.setUser_email(Common.current_user.getUser_email());

                        Double second_money_text = Double.valueOf(money_amount.getText().toString());

                        myCash_card.setMycash_card(String.valueOf((database_money_card-first_money_text) + second_money_text));
                        database.myInDao().updateMyInMoney(myInMoney);
                        database.myCashDao().updateMycashKart(myCash_card);
                        moneyList.add(myInMoney);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Qeyd edildi", Toast.LENGTH_SHORT).show();
                    }


                }


                if (myInMoney.getMoney_kind().equals("Nağd")) {

                    if (second_money_kind_text.equals("Kart")) {
                        moneyList.remove(myInMoney);

                        myInMoney.setMoney_date(note_date.toString());
                        myInMoney.setMoney_description(myInMoney.getMoney_description());
                        myInMoney.setMoney_amount(money_amount.getText().toString());
                        myInMoney.setMoney_kind(spinner_update.getSelectedItem().toString());
                        myInMoney.setUser_email(Common.current_user.getUser_email());


                        myCash_nagd.setMycash_card(String.valueOf(database_money_nagd - Double.valueOf(money_amount.getText().toString())));
                        myCash_card.setMycash_card(String.valueOf(database_money_card + Double.valueOf(money_amount.getText().toString())));

                        database.myCashDao().updateMycashNagd(myCash_nagd);
                        database.myCashDao().updateMycashKart(myCash_card);
                        database.myInDao().updateMyInMoney(myInMoney);

                        moneyList.add(myInMoney);
                        notifyDataSetChanged();


                        Toast.makeText(context, "Qeyd edildi", Toast.LENGTH_SHORT).show();

                    } else {
                        moneyList.remove(myInMoney);

                        myInMoney.setMoney_date(note_date.toString());
                        myInMoney.setMoney_description(myInMoney.getMoney_description());
                        myInMoney.setMoney_amount(money_amount.getText().toString());
                        myInMoney.setMoney_kind(spinner_update.getSelectedItem().toString());
                        myInMoney.setUser_email(Common.current_user.getUser_email());

                        Double second_money_text = Double.valueOf(money_amount.getText().toString());


                        myCash_nagd.setMycash_card(String.valueOf((database_money_nagd-first_money_text) + second_money_text));

                        database.myInDao().updateMyInMoney(myInMoney);
                        database.myCashDao().updateMycashNagd(myCash_nagd);
                        moneyList.add(myInMoney);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Qeyd edildi", Toast.LENGTH_SHORT).show();
                    }


                }


            }
        });


        builder.setView(view);
        builder.show();
    }

    private Double findDifferenceMoney(Double first_money_text, Double second_money_text) {

        if (first_money_text > second_money_text) {
            return first_money_text - second_money_text;
        } else if (second_money_text > first_money_text) {
            return second_money_text - first_money_text;
        } else {
            return 0.0;
        }

    }


    @Override
    public int getItemCount() {
        return moneyList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView money_date;
        public TextView money_amount;
        public TextView money_kind;
        public TextView money_description;

        public ImageButton money_update;
        public ImageButton money_delete;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            money_date = itemView.findViewById(R.id.money_date);
            money_amount = itemView.findViewById(R.id.money_amount);
            money_kind = itemView.findViewById(R.id.money_kind);
            money_description = itemView.findViewById(R.id.money_description);

            money_update = itemView.findViewById(R.id.money_update);
            money_delete = itemView.findViewById(R.id.money_delete);
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
