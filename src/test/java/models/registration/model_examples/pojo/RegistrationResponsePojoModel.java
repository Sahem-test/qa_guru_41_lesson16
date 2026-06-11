package models.registration.model_examples.pojo;

import static java.lang.String.format;

public class RegistrationResponsePojoModel {
    int id;
    String username;
    String firstName;
    String lastName;
    String email;
    String remoteAddr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    @Override
    public String toString() {
        return format("{\"id\": %d,\"username\": \"%s\",\"firstName\": \"%s\",\"lastName\": \"%s\"," +
                "\"email\": \"%s\",\"remoteAddr\": \"%s\"}",this.id, this.username, this.firstName, this.lastName,
                this.email,this.remoteAddr);
    }
}
