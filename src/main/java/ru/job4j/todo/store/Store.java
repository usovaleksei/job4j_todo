package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.List;

public interface Store {

    Item add(Item item);

    void update(int id, boolean done);

    Collection<Item> findAllItems();

    Collection<Item> findNotDoneItems();

    Item findItemById(int id);

    User save(User user);

    User findUserByPhone(String phone);

    List<Item> getUserItems(User user);

    List<Item> getUserNotDoneItems(User user);
}
