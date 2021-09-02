package ru.job4j.todo.servlet;

import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RegServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");

        if (HbmStore.getInstance().findUserByPhone(phone) == null) {
            HbmStore.getInstance().save(new User(name, phone, password));
            req.getRequestDispatcher("/login.html").forward(req, resp);
        } else {
            req.setAttribute("error", "Пользователь с таким телефоном уже зарегистрирован");
            req.getRequestDispatcher("reg.html").forward(req, resp);
        }
    }
}
