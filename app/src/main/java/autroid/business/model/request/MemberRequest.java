package autroid.business.model.request;

import java.util.ArrayList;

public class MemberRequest {

    private String contact_no,name,email,department,role;

    private ArrayList<String> permissions;

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }
}
