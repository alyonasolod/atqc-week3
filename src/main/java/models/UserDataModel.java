package models;

public class UserDataModel {
    private Integer id;
    private String name;
    private String email;
    private String gender;
    private String status;

    public UserDataModel(){

    }
    public UserDataModel(String name, String email, String gender){
        this(null,name,email,gender,"active");
    }

    public UserDataModel(Integer id, String name, String email, String gender, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getStatus() {
        return status;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
