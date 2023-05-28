package Project.Service.impl;

import Project.Class.Role;
import Project.Class.Users;
import Project.Exception.ExceptionUsers;
import Project.Service.UserService;
import Project.Validator.UsersValidator;
import Project.repository.RepositoryUser;

public class UserServiceImpl implements UserService {
    private final RepositoryUser repositoryUser;
    private final UsersValidator usersValidator;
    public UserServiceImpl(RepositoryUser repositoryUser) {
        this.repositoryUser = repositoryUser;
        usersValidator = new UsersValidator();
    }

    @Override
    public Users createUsers(String login, String password, String name, String surName) throws ExceptionUsers {
        usersValidator.checkLogin(login);
        Users users = new Users(login, password, name, surName);
        users.setRole(Role.CLIENT);
        repositoryUser.serialization(users);
        return users;
    }

    public Users cheakUsers(String login, String password) throws ExceptionUsers {
        return repositoryUser.loadUsers(login, password);
    }
}
