package autroid.business.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class JobsLogBE implements Serializable {
    private String stage;
    private ArrayList<JobsSubStatus> list;

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public ArrayList<JobsSubStatus> getList() {
        return list;
    }

    public void setList(ArrayList<JobsSubStatus> list) {
        this.list = list;
    }

    public class JobsSubStatus implements Serializable{
        private String user,name,stage,activity,created_at,updated_at;

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getActivity() {
            return activity;
        }

        public void setActivity(String activity) {
            this.activity = activity;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }
    }
}
