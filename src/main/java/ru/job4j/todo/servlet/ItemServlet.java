package ru.job4j.todo.servlet;

import org.json.JSONArray;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;

public class ItemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");

        User user = (User) req.getSession().getAttribute("user");

        Collection<Item> items = HbmStore.getInstance().getUserItems(user);
        resp.getWriter().println(new JSONArray(items));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");

        String description = req.getParameter("description");

        User user = (User) req.getSession().getAttribute("user");

        Item item = new Item(description, Timestamp.valueOf(LocalDateTime.now()), false);
        item.setUser(user);

        HbmStore.getInstance().add(item);

        resp.getWriter().write("Задача успешно сохранена");
    }
}
