package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.Collection;

public interface Store {

    Item add(Item item);

    void update(int id, boolean done);

    Collection<Item> findAllItems();

    Collection<Item> findNotDoneItems();

    Item findItemById(int id);

}
