package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.RequirementBE;

public class RequirementsAdapter extends RecyclerView.Adapter<RequirementsAdapter.MyViewHolder> {

    private ArrayList<RequirementBE> arrayList;
    Context context;
    public RequirementsAdapter(ArrayList<RequirementBE> arrayList){
        this.arrayList=arrayList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        EditText editText;
        ImageView remove;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            editText=itemView.findViewById(R.id.req_edit);
            remove=itemView.findViewById(R.id.remove);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    arrayList.get(getAdapterPosition()).setText(editable.toString());
                    Log.e("ChangedText",editable.toString());
                }
            });

            remove.setOnClickListener(v->{
                if(arrayList.size()!=1) {
                    arrayList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        public void onBind(int position){
            editText.setText(arrayList.get(position).getText());
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.requirements_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        if (arrayList.get(position).getFocused()) {
            myViewHolder.editText.post(new Runnable() {
                @Override
                public void run() {
                    if (myViewHolder.editText.requestFocus()) {
                        //setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                        InputMethodManager inputMethodManager = (InputMethodManager) myViewHolder.editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(myViewHolder.editText, InputMethodManager.SHOW_IMPLICIT);
                    }
                }
            });
            arrayList.get(position).setFocused(false);
        }
        myViewHolder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
