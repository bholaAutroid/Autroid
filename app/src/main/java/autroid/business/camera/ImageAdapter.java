package autroid.business.camera;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import com.bumptech.glide.request.RequestOptions;
//import com.qiscus.nirmana.Nirmana;

import java.util.ArrayList;

import autroid.business.R;
import autroid.business.model.bean.InspectionImageBE;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{

    ArrayList<InspectionImageBE> arrayList=null;
    RecyclerViewListener recyclerViewListener=null;

    public ImageAdapter(ArrayList<InspectionImageBE> arrayList, RecyclerViewListener recyclerViewListener){
        this.arrayList=arrayList;
        this.recyclerViewListener=recyclerViewListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
           image=itemView.findViewById(R.id.image);
           image.setOnClickListener(v-> recyclerViewListener.onItemImage(arrayList.get(getAdapterPosition()).getImgUrl(),arrayList.get(getAdapterPosition()).getIndex()));

        }

        public void bind(int i){
//            Nirmana.getInstance().get()
//                    .setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.placeholder_big))
//                    .load(arrayList.get(i).getImgUrl())
//                    .into(image);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View V= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.display_image, viewGroup, false);
        return new MyViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
