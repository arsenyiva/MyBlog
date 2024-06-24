package com.iva.blog.util;

import com.iva.blog.models.User;
import com.iva.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Валидатор для проверки пользовательских данных.
 */
@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Поддерживает ли валидатор проверку указанного класса.
     *
     * @param clazz класс для проверки поддержки валидатором.
     * @return true, если класс поддерживается валидатором, иначе false.
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }


    /**
     * Выполняет валидацию объекта target и добавляет ошибки в объект Errors при наличии недопустимостей.
     *
     * @param target объект для валидации (пользователь).
     * @param errors объект для добавления ошибок в процессе валидации.
     */
    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User existingUser = userRepository.findByUsername(user.getUsername()).orElse(null);
        if (existingUser != null) {
            errors.rejectValue("username", "", "Имя уже занято!");
        }

        checkPasswordLength(user.getPassword(), errors);
    }


    /**
     * Проверяет длину пароля и добавляет ошибку, если длина не соответствует заданным параметрам.
     *
     * @param password пароль для проверки.
     * @param errors   объект для добавления ошибок при недопустимости пароля.
     */
    private void checkPasswordLength(String password, Errors errors) {
        int minLength = 6;
        int maxLength = 25;
        if (password.length() < minLength || password.length() > maxLength) {
            errors.rejectValue("password", "", "Длина пароля должна быть от " + minLength + " до " + maxLength + " символов.");
        }
    }
}

