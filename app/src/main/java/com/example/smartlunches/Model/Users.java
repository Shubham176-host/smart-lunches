package com.example.smartlunches.Model;

public class Users
{
    private String name, password, image, email ,usn ,  semester , branch , phone;

    public  Users(){

    }

    public Users(String name, String password, String image, String email, String usn , String semester , String branch, String phone)
    {
        this.name = name;
        this.password = password;
        this.image = image;
        this.email = email;
        this.usn = usn;
        this.semester = semester;
        this.branch = branch;
        this.phone = phone;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String address)
    {
        this.email = email;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
