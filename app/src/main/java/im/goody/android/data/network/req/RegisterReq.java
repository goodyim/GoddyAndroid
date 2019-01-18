package im.goody.android.data.network.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("user")
public class RegisterReq {
    @JsonProperty("user_name")
    private String name;
    private String email;
    private String password;

    private String phoneNumber;

    private int sex;

    private String birthday;


    // ======= region getters =======
    
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getSex() {
        return sex;
    }
    public String getBirthday() {
        return birthday;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    // endregion

    // ======= region setters =======
    public RegisterReq setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public RegisterReq setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public RegisterReq setName(String name) {
        this.name = name;
        return this;
    }

    public RegisterReq setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterReq setPassword(String password) {
        this.password = password;
        return this;
    }


    public RegisterReq setSex(int sex) {
        this.sex = sex;
        return this;
    }

    //endregion
}
