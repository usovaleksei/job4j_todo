package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.Collection;

public class StubStore implements Store {

    @Override
    public Item add(Item item) {
        return null;
    }

    @Override
    public Collection<Item> findAllItems() {
        return null;
    }

    @Override
    public Collection<Item> findNotDoneItems() {
        return null;
    }

    @Override
    public Item findItemById(int id) {
        return null;
    }

    @Override
    public void update(Item item) {

    }
}
