package project.service;

import project.entity.Users;
import project.exception.ExceptionUsers;

public interface UserService {
    Users createUsers(String login, String password, String name, String surName) throws ExceptionUsers;

    Users cheakUsers(String login, String password) throws ExceptionUsers;
}
