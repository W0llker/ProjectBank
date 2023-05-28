package Project.Service;

import Project.Class.Users;
import Project.Exception.ExceptionUsers;

public interface UserService {
    Users createUsers(String login, String password, String name, String surName) throws ExceptionUsers;

    Users cheakUsers(String login, String password) throws ExceptionUsers;
}
