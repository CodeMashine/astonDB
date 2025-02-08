package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HiberWorker {
    private SessionFactory sessionFactory;

    private void setUp() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.current_session_context_class", "thread")
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
                            .addAnnotatedClass(Person.class)
                            .addAnnotatedClass(Role.class)
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public void hello() {
        setUp();
        System.out.println("Hibernate Worker Hello");
    }


    public void addPerson() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        try {
            Role admin = getOrCreateRole(session, "admin");
            Role user = getOrCreateRole(session, "user");

            Person one = new Person("test", 88);
            Person two = new Person("rtewrtw", 35);

            one.addRole(admin, user);
            two.addRole(user);

            session.persist(one);
            session.persist(two);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }


    private Role getOrCreateRole(Session session, String name) {
        Role role = session.createQuery("FROM Role WHERE role = :name", Role.class)
                .setParameter("name", name)
                .uniqueResult();

        if (role == null) {
            role = new Role(name);
            session.persist(role);
        }

        return role;
    }
}
