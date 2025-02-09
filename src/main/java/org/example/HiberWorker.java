package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HiberWorker {
    private static SessionFactory sessionFactory;

    private static void setUp() {
        final StandardServiceRegistry registry =
                new StandardServiceRegistryBuilder()
                        .applySetting("hibernate.current_session_context_class", "thread")
                        .build();
        try {
            sessionFactory =
                    new MetadataSources(registry)
//                            .addAnnotatedClass(Person.class)
//                            .addAnnotatedClass(Role.class)
                            .addPackage("org.example")
                            .buildMetadata()
                            .buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void init() {
        setUp();
        System.out.println("Hibernate start");
    }


    public static void addPerson(String name, String age, String roles) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Person person = new Person(name, age);

            Role admin = getOrCreateRole(session, "admin");
            Role user = getOrCreateRole(session, "user");

            for (String role : parseRoles(roles)) {
                Role roleToAdd = getOrCreateRole(session, role);
                person.addRole(roleToAdd);
            }

            session.persist(person);

            transaction.commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private static String[] parseRoles(String roles) {
        return roles.split(" ");
    }

    private static Role getOrCreateRole(Session session, String name) {
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
