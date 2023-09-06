package project.repository;

import project.entity.Users;
import project.exception.ExceptionUsers;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RepositoryUser {
    private final static File file = new File("resources/users");
    public static List<Users> usersList;

    public RepositoryUser() {
        usersList = new ArrayList<>();
    }

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
