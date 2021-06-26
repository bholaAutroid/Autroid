package autroid.business.model.request;

public class UpdateCustomerDetailsRequest {

    private String user,name,contact_no,email,company_name,gstin;

    public void setUser(String user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact_no(String contact_no) {
        this.contact_no = contact_no;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }
}
