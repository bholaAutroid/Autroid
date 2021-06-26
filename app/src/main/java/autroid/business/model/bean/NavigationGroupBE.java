package autroid.business.model.bean;

import java.util.ArrayList;

public class NavigationGroupBE {

        private String module;

        private ArrayList<NavigationItemsBE> group;

        public String getModule() {
            return module;
        }

        public void setModule(String module) {
            this.module = module;
        }

        public ArrayList<NavigationItemsBE> getGroup() {
            return group;
        }

        public void setGroup(ArrayList<NavigationItemsBE> group) {
            this.group = group;
        }


}
