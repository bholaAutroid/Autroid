package autroid.business.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.OnAccountCallback;
import autroid.business.model.realm.UserRealm;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class AccountsListAdapter extends RealmRecyclerViewAdapter<UserRealm,AccountsListAdapter.AccountsHolder> {

    Context context;
    private OnAccountCallback mOnAccountCallback;

    public AccountsListAdapter(@Nullable OrderedRealmCollection<UserRealm> data, boolean autoUpdate, Context context,OnAccountCallback mOnAccountCallback) {
        super(data, autoUpdate);
        this.context=context;
        this.mOnAccountCallback=mOnAccountCallback;
    }

    @NonNull
    @Override
    public AccountsHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.
                from(context).
                inflate(R.layout.row_accounts_list, parent, false);

        return new AccountsHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull AccountsHolder accountsHolder, int i) {


        UserRealm userRealm=getItem(i);
        accountsHolder.userRealm=userRealm;
        accountsHolder.mName.setText(userRealm.getName()+" ("+userRealm.getContactNo()+")");
    }


    public  class AccountsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mName;
        RelativeLayout mMainLayout;
        UserRealm userRealm;


        public AccountsHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.account_name);
            mMainLayout = itemView.findViewById(R.id.ll_main);
            mMainLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_main:
                    mOnAccountCallback.onAccountClick(userRealm.getId(),userRealm.getLoggedIn());
                    break;

            }
        }
    }
    }