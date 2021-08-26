package ru.job4j.todo.servlet;

import org.json.JSONArray;
import ru.job4j.todo.model.Item;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");
        Collection<Item> items = HbmStore.getInstance().findAllItems();
        resp.getWriter().println(new JSONArray(items));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        HbmStore.getInstance().add(new Item(description, Timestamp.valueOf(LocalDateTime.now()), false));
        resp.getWriter().write("Задача успешно сохранена");
    }
}
