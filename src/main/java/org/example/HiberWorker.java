package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class HiberWorker {
    private static SessionFactory sessionFactory;

    static {
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
            e.printStackTrace();
        }
    }

    public static void init() {
        System.out.println("Hibernate start");
    }


    public static void addPerson(String name, String age, String roles) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        try {
            Person person = new Person(name, age);

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


    public static List<Person> getAllPersons() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT p FROM Person p JOIN FETCH p.roles", Person.class).list();
        }
    }

    public static void deletePerson(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession();) {
            transaction = session.beginTransaction();
            Person person = session.get(Person.class, id);
            if (person != null) {
                person.resetRoles();
                session.delete(person);
                transaction.commit();
            } else {
                throw new RuntimeException("Person with id " + id + " not found");
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Person getPersonForId(int id) {
        Session session = sessionFactory.openSession();
        Person person = session.get(Person.class, id);
        return person;
    }

    public static Person updatePerson(int id, String fullname, String age, String roles) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            Person person = session.get(Person.class, id);
            if (person != null) {
                person.setFullName(fullname);
                person.setAge(age);
                person.resetRoles();
                for (String role : parseRoles(roles)) {
                    Role roleToAdd = getOrCreateRole(session, role);
                    person.addRole(roleToAdd);
                }
            } else {
                throw new RuntimeException("Person with id " + id + " not found");
            }
            transaction.commit();
            return person;
        } finally {
            session.close();
        }
    }

}
