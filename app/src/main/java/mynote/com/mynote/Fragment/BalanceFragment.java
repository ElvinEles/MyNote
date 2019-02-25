package mynote.com.mynote.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mynote.com.mynote.Database.Local.MyDatabase;
import mynote.com.mynote.R;


public class BalanceFragment extends Fragment {

    private MyDatabase database;

    private static BalanceFragment INSTANCE = null;


    public static BalanceFragment getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BalanceFragment();

        }
        return INSTANCE;

    }

    public BalanceFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_balance, container, false);

        database = MyDatabase.getInstance(getContext());

        Double mycash = Double.valueOf(database.myCashDao().getMycashKart().getMycash_card()) + Double.valueOf(database.myCashDao().getMycashNagd().getMycash_card());


        TextView my_money = view.findViewById(R.id.my_money);
        TextView my_money_card = view.findViewById(R.id.my_money_card);
        TextView my_money_cash = view.findViewById(R.id.my_money_cash);


        my_money.setText(new StringBuilder(String.valueOf(mycash)).append(" AZN"));
        my_money_card.setText(new StringBuilder(String.valueOf(database.myCashDao().getMycashKart().getMycash_card())).append(" AZN"));
        my_money_cash.setText(new StringBuilder(String.valueOf(database.myCashDao().getMycashNagd().getMycash_card())).append(" AZN"));


        return view;
    }


}






































