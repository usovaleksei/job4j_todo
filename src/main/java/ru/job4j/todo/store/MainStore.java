package ru.job4j.todo.store;

import ru.job4j.todo.model.Item;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MainStore {

    public static void main(String[] args) {
        //Store store = new HbmStore();
        Item itemOne = new Item("Выучить Java", Timestamp.valueOf(LocalDateTime.now()), false);
        Item itemTwo = new Item("Выучить Git", Timestamp.valueOf(LocalDateTime.now()), true);
        /*store.add(itemOne);
        store.add(itemTwo);*/
        /*for (Item item : store.findAllItems()) {
            System.out.println(item);
        }*/
    }
}
