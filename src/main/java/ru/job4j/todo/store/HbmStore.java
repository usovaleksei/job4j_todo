package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * class for work with db with Hibernate
 *
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
     *
     * @param item - creating item
     * @return - item, which was adding to db
     */
    @Override
    public Item add(Item item) {
        return this.tx(
                session -> {
                    session.save(item);
                    return item;
                }
        );
    }

    /**
     * method update item status
     *
     * @param id   - item id for update
     * @param done - new item ststus
     */
    @Override
    public void update(int id, boolean done) {
        this.tx(
                session -> {
                    var query = session.createQuery(
                            "update Item set done = :done where id = :id"
                    );
                    query.setParameter("done", done);
                    query.setParameter("id", id);
                    query.executeUpdate();
                    return id;
                }
        );
    }

    /**
     * method return all items from db
     *
     * @return - all items from db
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Item> findAllItems() {
        return this.tx(
                session -> session.createQuery("from ru.job4j.todo.model.Item").list()
        );
    }

    /**
     * method get all not done items
     *
     * @return items, which not done
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Item> findNotDoneItems() {
        return this.tx(
                session ->
                        session.createQuery(
                                "from ru.job4j.todo.model.Item where done = false")
                        .list()
        );
    }

    /**
     * method get all items by authorization user
     * @param user authorization user
     * @return user items list
     */
    @Override
    public List<Item> getUserItems(User user) {
        return this.tx(
                session ->
                session.createQuery("from ru.job4j.todo.model.Item where user = :user")
                        .setParameter("user", user)
                        .list()
        );
    }

    /**
     * method get not done items by authorization user
     * @param user authorization user
     * @return user not done items list
     */
    @Override
    public List<Item> getUserNotDoneItems(User user) {
        return this.tx(
                session ->
                session.createQuery(
                        "from ru.job4j.todo.model.Item where user = :user and done = false")
                        .setParameter("user", user)
                        .list()
        );
    }

    /**
     * method find item by id from db
     *
     * @param id - id item
     * @return - item
     */
    @Override
    public Item findItemById(int id) {
        return this.tx(
                session -> session.get(Item.class, id)
        );
    }

    /**
     * method save new user to db
     *
     * @param user - new user
     * @return - user, which saved
     */
    @Override
    public User save(User user) {
        return this.tx(
                session -> {
                    session.save(user);
                    return user;
                }
        );
    }

    /**
     * method find user from db dy phone
     *
     * @param phone - user phone
     * @return - found user
     */
    @Override
    public User findUserByPhone(String phone) {
        return this.tx(
                session -> {
                    var query = session.createQuery(
                            "from ru.job4j.todo.model.User where phone = :phone"
                    );
                    query.setParameter("phone", phone);
                    return (User) query.uniqueResult();
                }
        );
    }

    /**
     * method implements the decorator pattern
     *
     * @param command function to execute
     * @param <T>     data type
     * @return function result
     */
    private <T> T tx(final Function<Session, T> command) {
        final Session session = this.sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            LOG.error("Operation error");
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(this.registry);
    }
}
