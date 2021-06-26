package autroid.business.adapter.cars;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.camera.RecyclerViewListener;
import autroid.business.model.bean.CarDocumentBE;

public class CarDocumentAdapter extends RecyclerView.Adapter<CarDocumentAdapter.MyViewHolder> {

    //The getView method is called for every element
    //then for every element it calls on create
    //If we don't use getView the directly on create view is called only once
    //giving same view for every item of arraylist

    private static final int PDF = 1;

    private static final int IMAGE = 2;

    private static final int DOC = 3;

    private static final int DOCX = 4;

    RecyclerViewListener recyclerViewListener;

    private Context context;

    private ArrayList<CarDocumentBE> arrayList;

    public CarDocumentAdapter(ArrayList<CarDocumentBE> arrayList, RecyclerViewListener recyclerViewListener) {
        this.arrayList = arrayList;
        this.recyclerViewListener = recyclerViewListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView caption;

        ImageView image, delete;

        public MyViewHolder(View itemView, int viewHolderType) {

            super(itemView);

            if (viewHolderType == PDF || viewHolderType==DOC) {
                caption = itemView.findViewById(R.id.caption);
                delete = itemView.findViewById(R.id.delete);
                image = itemView.findViewById(R.id.image);
                image.setOnClickListener(v -> recyclerViewListener.onDocumentClick(arrayList.get(getAdapterPosition()).getFile_address(), getAdapterPosition(), arrayList.get(getAdapterPosition()).getFile_type()));
                delete.setOnClickListener(v -> recyclerViewListener.onDocumentDelete(arrayList.get(getAdapterPosition()).getId(), getAdapterPosition()));
            } else {
                image = itemView.findViewById(R.id.image);
                delete = itemView.findViewById(R.id.delete);
                caption = itemView.findViewById(R.id.caption);
                delete.setVisibility(View.VISIBLE);
                caption.setVisibility(View.VISIBLE);
                image.setOnClickListener(v -> recyclerViewListener.onItemImage(arrayList.get(getAdapterPosition()).getFile_address(), getAdapterPosition()));
                delete.setOnClickListener(v -> recyclerViewListener.onImageDelete(arrayList.get(getAdapterPosition()).getId(), getAdapterPosition()));
            }
        }

        public void bind(int position) {

            CarDocumentBE document = arrayList.get(position);

            if (document.getFile_type().equalsIgnoreCase("PDF")){
                Picasso.with(context).load(R.drawable.placeholder).into(image);
            }else if( document.getFile_type().equalsIgnoreCase("DOC") || document.getFile_type().equalsIgnoreCase("DOCX") ){
                Picasso.with(context).load(R.drawable.doc_file).into(image);
            } else Picasso.with(context).load(arrayList.get(position).getFile_address()).into(image);

            caption.setText(document.getCaption());

        }
    }


    @Override
    public int getItemViewType(int position) {

        if (arrayList.get(position).getFile_type().equalsIgnoreCase("PDF")) return PDF;
        else if(arrayList.get(position).getFile_type().equalsIgnoreCase("DOC"))return DOC;
        else if(arrayList.get(position).getFile_type().equalsIgnoreCase("DOCX"))return DOCX;
        else return IMAGE;
    }

    //initializing text and time and tells what views(textviews, etc) the view holder will have
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int posByGetItemView) {

        int type = 0;

        View view = null;

        context = viewGroup.getContext();

        switch (posByGetItemView) {

            case PDF:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_pdf, viewGroup, false);
                type = PDF;
                break;

            case IMAGE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_image, viewGroup, false);
                type = IMAGE;
                break;

            case DOC:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_pdf, viewGroup, false);
                type = DOC;
                break;

            case DOCX:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_car_pdf, viewGroup, false);
                type = DOCX;
                break;
        }

        return new MyViewHolder(view, type);
    } //creating my custom view of message format

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(position);
    }

    //recyclerview will ask for data for a particular position we will take the data for the same postion from list
    //and will take a message format object out and set the values.

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}