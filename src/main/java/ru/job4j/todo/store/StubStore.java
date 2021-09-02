package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class StubStore implements Store {

    private static final StubStore INST = new StubStore();
    private static final AtomicInteger ITEM_ID = new AtomicInteger(4);
    private static final AtomicInteger USER_ID = new AtomicInteger(4);
    private final Map<Integer, Item> items = new ConcurrentHashMap<>();
    private final Map<Integer, User> users = new ConcurrentHashMap<>();

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
    public List<Item> getUserItems(User user) {
        return this.items.values().stream()
                .filter(item -> user.getUserId() == item.getUser().getUserId())
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getUserNotDoneItems(User user) {
        return this.items.values().stream()
                .filter(item -> !item.isDone() && (user.getUserId() == item.getUser().getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(int id) {
        return this.items.get(id);
    }

    @Override
    public User save(User user) {
        user.setUserId(USER_ID.incrementAndGet());
        this.users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public User findUserByPhone(String phone) {
        return this.users.get(phone);
    }
}
