package autroid.business.adapter.jobcard;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import autroid.business.R;
import autroid.business.interfaces.JobCardCallback;
import autroid.business.model.realm.JobsRealm;
import autroid.business.utils.Constant;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class JobsListAdapter extends RealmRecyclerViewAdapter<JobsRealm, JobsListAdapter.JobsListHolder> {

    Context context;
    JobCardCallback mCallback;
    boolean isTechnician;

    public JobsListAdapter(@Nullable OrderedRealmCollection<JobsRealm> data, boolean autoUpdate,JobCardCallback mCallback,boolean isTechnician) {
        super(data, autoUpdate);
        this.mCallback=mCallback;
        this.isTechnician=isTechnician;
    }

    @NonNull
    @Override
    public JobsListHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        context=parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_jobs_list, parent, false);

        return new JobsListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobsListHolder holder, int i) {
        JobsRealm jobsRealm=getItem(i);
        holder.jobsRealm=jobsRealm;
        holder.mJobNo.setText("#"+jobsRealm.getBookingNo());
        holder.mTimeLeft.setText(jobsRealm.getTimeLeft());
        holder.mCarInfo.setText(jobsRealm.getRegistrationNo()+" / "+jobsRealm.getTitle());
        holder.mUserInfo.setText(jobsRealm.getUserName());
        if(jobsRealm.getSecondaryStatus().equals(Constant.JOB_INITIATED))holder.mBtnCamera.setVisibility(View.GONE);
    }

    public class JobsListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mJobNo,mTimeLeft,mCarInfo,mUserInfo;
        ImageView mBtnChat,mBtnCall,mBtnCamera;
        ConstraintLayout mMainLayout;
        JobsRealm jobsRealm;

        public JobsListHolder(@NonNull View itemView) {
            super(itemView);

            mJobNo=itemView.findViewById(R.id.job_number);
            mTimeLeft=itemView.findViewById(R.id.days_left);
            mCarInfo=itemView.findViewById(R.id.car_registration_no);
            mUserInfo=itemView.findViewById(R.id.user_info);
            mMainLayout=itemView.findViewById(R.id.main_layout);
            mBtnChat=itemView.findViewById(R.id.btn_chat);
            mBtnCall=itemView.findViewById(R.id.btn_call);
            mBtnCamera=itemView.findViewById(R.id.btn_camera);

            mMainLayout.setOnClickListener(this);
            mBtnChat.setOnClickListener(this);
            mBtnCall.setOnClickListener(this);
            mBtnCamera.setOnClickListener(this);

            if(isTechnician){
                mBtnCall.setVisibility(View.GONE);
                mBtnChat.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.main_layout:
                    mCallback.onJobClick(jobsRealm.getId());
                    break;

                case R.id.btn_chat:
                    mCallback.onChatClick(jobsRealm.getUserMobile());
                    break;

                case R.id.btn_call:
                    mCallback.onCallClick(jobsRealm.getUserMobile());
                    break;

                case R.id.btn_camera:
                    mCallback.onCameraClick(jobsRealm.getId());
                    break;
            }
        }
    }
}
