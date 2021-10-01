package karbanovich.fit.bstu.birthdays;

public class Contact {
    private String name;
    private String surname;
    private String phoneNumber;
    private String birthday;    //YYYY.MM.DD


    public Contact(String name, String surname, String phoneNumber, String birthday) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
    }

    public String getName() {return name;}
    public String getSurname() {return surname;}
    public String getPhoneNumber() {return phoneNumber;}
    public String getBirthday() {return birthday;}

    public void setName(String name) {this.name = name;}
    public void setSurname(String surname) {this.surname = surname;}
    public void setPhoneNumber(String phoneNumber) {this.phoneNumber = phoneNumber;}
    public void setBirthday(String birthday) {this.birthday = birthday;}
}
