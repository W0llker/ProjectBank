package Project.Validator;

import Project.Class.Users;
import Project.Exception.ExceptionUsers;
import Project.repository.RepositoryUser;

import java.util.List;
import java.util.Optional;

public class UsersValidator {
    private final RepositoryUser repositoryUser;
    public UsersValidator() {
        this.repositoryUser = new RepositoryUser();
    }

    public String checkLogin(String login) throws ExceptionUsers {
        List<Users> list = repositoryUser.deserialization();
        for (Users users : list) {
            if(users.getLogin().equals(login)) {
                throw new ExceptionUsers("Данный логин уже есть " + login);
            }
        }
        return login;
    }
    public boolean checkUsersId(long id) throws ExceptionUsers {
        Optional<Users> users = repositoryUser.deserialization().stream().filter(users1 -> users1.getId() == id).findAny();
        if(users.isEmpty()) {
            throw new ExceptionUsers("Данного пользователя нету");
        }
        return true;
    }
    public int checkUsersLogin(String login) throws ExceptionUsers {
        Optional<Users> users = repositoryUser.deserialization().stream().filter(users1 -> users1.getLogin().equals(login)).findAny();
        if(users.isEmpty()) {
            throw new ExceptionUsers("Данного пользователя нету");
        }
        return users.get().getId();
    }
}
