package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StubStore implements Store {

    private static final StubStore INST = new StubStore();
    private static final AtomicInteger ITEM_ID = new AtomicInteger(4);
    private final Map<Integer, Item> items = new ConcurrentHashMap<>();

    private StubStore() {
    }

    public static StubStore getInstance() {
        return INST;
    }

    @Override
    public Item add(Item item) {
        item.setId(ITEM_ID.incrementAndGet());
        this.items.put(item.getId(), item);
        return item;
    }

    @Override
    public void update(int id, boolean done) {
        Item item = this.items.get(id);
        item.setDone(done);
    }

    @Override
    public Collection<Item> findAllItems() {
        return this.items.values();
    }

    @Override
    public Collection<Item> findNotDoneItems() {
        return this.items.values().stream()
                .filter(item -> !item.isDone())
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(int id) {
        return this.items.get(id);
    }
}
