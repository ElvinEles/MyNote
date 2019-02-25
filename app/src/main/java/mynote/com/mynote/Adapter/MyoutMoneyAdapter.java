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
import mynote.com.mynote.Database.Model.MyOutMoney;
import mynote.com.mynote.R;

public class MyoutMoneyAdapter extends RecyclerView.Adapter<MyoutMoneyAdapter.MyViewHolder> {

    private Context context;
    private List<MyOutMoney> moneyList;
    private MyDatabase database;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private StringBuilder note_date;
    private MaterialEditText money_date;


    public MyoutMoneyAdapter(Context context, List<MyOutMoney> moneyList) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_myoutmoney_item, viewGroup, false);

        return new MyoutMoneyAdapter.MyViewHolder(view);
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
                MyOutMoney myoutMoney = moneyList.get(position);
                showUpdateBuilder(myoutMoney);
            }
        });

        myViewHolder.money_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.deleted_myoutmoney = moneyList.get(position);
                database.myOutDao().deleteMyOutMoney(moneyList.get(position));
                moneyList.remove(position);

                Snackbar snackbar = Snackbar.make(myViewHolder.money_delete, "Silinmiş qeydi geri qaytara bilərsiniz!!!", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.WHITE);


                snackbar.setAction("Geri", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        database.myOutDao().insertMyOutMoney(Common.deleted_myoutmoney);
                        moneyList.add(Common.deleted_myoutmoney);
                        notifyDataSetChanged();
                    }

                });

                snackbar.show();

                notifyDataSetChanged();
            }
        });

    }

    private void showUpdateBuilder(final MyOutMoney myoutMoney) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Yeniləyin");

        View view = LayoutInflater.from(context).inflate(R.layout.layout_update_myoutmoney_item, null, false);

        money_date = view.findViewById(R.id.money_date);
        final MaterialEditText money_amount = view.findViewById(R.id.money_amount);
        final MaterialEditText money_description = view.findViewById(R.id.money_description);

        final Spinner spinner_update = view.findViewById(R.id.money_kind);

        ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(context, R.array.kind, android.R.layout.simple_spinner_item);
        list.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_update.setAdapter(list);


        money_date.setText(myoutMoney.getMoney_date());
        money_amount.setText(myoutMoney.getMoney_amount());
        money_description.setText(myoutMoney.getMoney_description());
        spinner_update.setSelection(list.getPosition(myoutMoney.getMoney_kind()));

        final Double first_money_text = Double.valueOf(myoutMoney.getMoney_amount());
        String first_money_kind_text = myoutMoney.getMoney_kind();


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

                //card money

                if (myoutMoney.getMoney_kind().equals("Kart")) {


                    if (second_money_kind_text.equals("Nağd")) {

                        String nagd_changed_money = find_changed_money(database_money_nagd, first_money_text);
                        String card_chaged_add_money = findchangedAddMoneyCard(database_money_card, first_money_text);

                        moneyList.remove(myoutMoney);

                        myoutMoney.setMoney_date(note_date.toString());
                        myoutMoney.setMoney_description(money_description.getText().toString());
                        myoutMoney.setMoney_kind(myoutMoney.getMoney_kind());
                        myoutMoney.setMoney_amount(first_money_text.toString());
                        myoutMoney.setUser_email(Common.current_user.getUser_email());

                        myCash_card.setMycash_card(card_chaged_add_money);
                        myCash_nagd.setMycash_card(nagd_changed_money);

                        database.myOutDao().updateMyOutMoney(myoutMoney);
                        database.myCashDao().updateMycashKart(myCash_card);
                        database.myCashDao().updateMycashNagd(myCash_nagd);

                        moneyList.add(myoutMoney);
                        notifyDataSetChanged();


                    } else {
                        final Double first_edittext_money = Double.valueOf(money_amount.getText().toString());

                        Double difference_money_of_edittext = findDifferenceMoney(first_money_text, first_edittext_money);

                        String card_changed_money = find_changed_money(database_money_card, difference_money_of_edittext);

                        moneyList.remove(myoutMoney);

                        myoutMoney.setMoney_date(note_date.toString());
                        myoutMoney.setMoney_description(money_description.getText().toString());
                        myoutMoney.setMoney_kind(myoutMoney.getMoney_kind());
                        myoutMoney.setMoney_amount(first_edittext_money.toString());
                        myoutMoney.setUser_email(Common.current_user.getUser_email());

                        myCash_card.setMycash_card(card_changed_money);

                        database.myOutDao().updateMyOutMoney(myoutMoney);
                        database.myCashDao().updateMycashKart(myCash_card);
                        moneyList.add(myoutMoney);
                        notifyDataSetChanged();
                    }

                }
                //card money

                if (myoutMoney.getMoney_kind().equals("Nağd")) {

                    if (second_money_kind_text.equals("Kart")) {

                        String card_changed_money = find_changed_money(database_money_card, first_money_text);
                        String nagd_chaged_add_money = findchangedAddMoneyCard(database_money_nagd, first_money_text);

                        moneyList.remove(myoutMoney);

                        myoutMoney.setMoney_date(note_date.toString());
                        myoutMoney.setMoney_description(money_description.getText().toString());
                        myoutMoney.setMoney_kind(myoutMoney.getMoney_kind());
                        myoutMoney.setMoney_amount(first_money_text.toString());
                        myoutMoney.setUser_email(Common.current_user.getUser_email());

                        myCash_card.setMycash_card(card_changed_money);
                        myCash_nagd.setMycash_card(nagd_chaged_add_money);

                        database.myOutDao().updateMyOutMoney(myoutMoney);
                        database.myCashDao().updateMycashKart(myCash_card);
                        database.myCashDao().updateMycashNagd(myCash_nagd);

                        moneyList.add(myoutMoney);
                        notifyDataSetChanged();

                    }else {

                        final Double first_edittext_money = Double.valueOf(money_amount.getText().toString());

                        Double difference_money_of_edittext = findDifferenceMoney(first_money_text, first_edittext_money);

                        String nagd_changed_money = find_changed_money(database_money_nagd, difference_money_of_edittext);

                        moneyList.remove(myoutMoney);

                        myoutMoney.setMoney_date(note_date.toString());
                        myoutMoney.setMoney_description(money_description.getText().toString());
                        myoutMoney.setMoney_kind(myoutMoney.getMoney_kind());
                        myoutMoney.setMoney_amount(first_edittext_money.toString());
                        myoutMoney.setUser_email(Common.current_user.getUser_email());

                        myCash_nagd.setMycash_card(nagd_changed_money);

                        database.myOutDao().updateMyOutMoney(myoutMoney);
                        database.myCashDao().updateMycashNagd(myCash_nagd);
                        moneyList.add(myoutMoney);
                        notifyDataSetChanged();
                    }

                }


                Toast.makeText(context, "Qeyd edildi", Toast.LENGTH_SHORT).show();



            }
        });


        builder.setView(view);
        builder.show();
    }

    private Double findDifferenceMoney(Double first_money_text, Double first_edittext_money) {
        if (first_money_text > first_edittext_money) {
            return first_money_text - first_edittext_money;
        } else if (first_edittext_money > first_money_text) {
            return first_edittext_money - first_money_text;
        } else {
            return 0.0;
        }
    }

    private String findchangedAddMoneyCard(Double database_money_card, Double first_money_text) {
        String money = String.valueOf(database_money_card + first_money_text);
        return money;
    }

    private String find_changed_money(Double database_money_card, Double first_money_text) {
        if (database_money_card > first_money_text && database_money_card>0) {
            String money = String.valueOf(database_money_card - first_money_text);
            return money;
        } else {
            Toast.makeText(context, "Kartda məbləğ azdır", Toast.LENGTH_SHORT).show();
            return null;
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
