package Project.Class;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Users implements Serializable {

    @Serial
    private static final long serialVersionUID = 6647665167316055778L;
    private int id;
    private String login;
    private String password;
    private String name;
    private String surName;
    private Role role;

    public Users(String login, String password, String name, String surName) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surName = surName;
    }
    public String userInformation() {
        return "Имя:" + name + " Фамилия:" + surName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Users users = (Users) o;
        return Objects.equals(login, users.login) && Objects.equals(name, users.name) && Objects.equals(surName, users.surName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, name, surName);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surName='" + surName + '\'' +
                ", role=" + role +
                "\n}";
    }
}
