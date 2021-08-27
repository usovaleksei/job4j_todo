package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.List;

/**
 * class for work with db with Hibernate
 * @author Aleksei Usov
 * @since 24/08/2021
 */

public class HbmStore implements Store, AutoCloseable {

    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();
    private static final Logger LOG = LoggerFactory.getLogger(HbmStore.class.getName());

    private HbmStore() {

    }

    private static final class Lazy {
        private static final Store INST = new HbmStore();
    }

    public static Store getInstance() {
        return Lazy.INST;
    }

    /**
     * method adding new item to db
     * @param item - creating item
     * @return - item, which was adding to db
     */
    @Override
    public Item add(Item item) {
        Session session = this.sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    /**
     * method update item status
     * @param id - item id for update
     * @param done - new item ststus
     */
    @Override
    public void update(int id, boolean done) {
        Session session = this.sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("update Item set done = :done where id = :id");
        query.setParameter("done", done);
        query.setParameter("id", id);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    /**
     * method return all items from db
     * @return - all items from db
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Item> findAllItems() {
        Session session = this.sf.openSession();
        session.beginTransaction();
        List<Item> itemList = session.createQuery("from ru.job4j.todo.model.Item").list();
        session.getTransaction().commit();
        session.close();
        return itemList;
    }


    /**
     * method get all not done items
     * @return items, which not done
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Item> findNotDoneItems() {
        Session session = this.sf.openSession();
        session.beginTransaction();
        List<Item> itemList = session.createQuery("from ru.job4j.todo.model.Item where done = false").list();
        session.getTransaction().commit();
        session.close();
        return itemList;
    }

    /**
     * method find item by id from db
     * @param id - id item
     * @return - item
     */
    @Override
    public Item findItemById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item result = session.get(Item.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(this.registry);
    }
}
