package com.iva.blog.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;


/**
 * Контроллер для обработки ошибок.
 */
@Controller
public class CustomErrorController implements ErrorController {

    /**
     * Обрабатывает запросы на URL /error.
     *
     * @param request объект HttpServletRequest для получения атрибутов запроса.
     * @return перенаправление на главную страницу в случае ошибки 404, либо на страницу ошибки в остальных случаях.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "redirect:/mainPage";
            }
        }
        return "error";
    }
}
