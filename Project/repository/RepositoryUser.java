package Project.repository;

import Project.Class.Accounts;
import Project.Class.Operation;
import Project.Class.Users;
import Project.Exception.ExceptionUsers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepositoryUser {
    private final static File file = new File("resources/users");
    public static List<Users> usersList = new ArrayList<>();

    public void serialization(Users users) {
        usersList = deserialization();
        Optional<Users> optionalId;
        if (usersList.size() > 0) {
            optionalId = usersList.stream().max(Comparator.comparingLong(Users::getId));
            users.setId(optionalId.get().getId() + 1);
        } else users.setId(0);
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            usersList.add(users);
            System.out.println(users);
            stream.writeObject(usersList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Users> deserialization() {
        try (ObjectInputStream stream = new ObjectInputStream(new FileInputStream(file))) {
            usersList = (List<Users>) stream.readObject();
        } catch (Exception e) {
        }
        return usersList;
    }

    public Users loadUsers(String login, String password) throws ExceptionUsers {
        usersList = deserialization();
        for (Users users : usersList) {
            if (users.getLogin().equals(login) && users.getPassword().equals(password)) {
                return users;
            } else if (users.getLogin().equals(login) && !users.getPassword().equals(password)) {
                throw new ExceptionUsers("Пароль не подходит");
            }
        }
        throw new ExceptionUsers("Пользователь не найден");
    }

    public Users getUsers(long id) throws ExceptionUsers {
        usersList = deserialization();
        for (Users users : usersList) {
            if (users.getId() == id) return users;
        }
        throw new ExceptionUsers("Нету такого клиента");
    }

    public void updateAccount() {
        try (ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(file))) {
            stream.writeObject(usersList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
