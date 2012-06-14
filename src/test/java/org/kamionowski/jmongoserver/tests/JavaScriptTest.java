package org.kamionowski.jmongoserver.tests;

import com.mongodb.BasicDBObject;
import org.junit.Assert;
import org.junit.Test;
import org.kamionowski.jmongoserver.tests.domain.Person;

import java.util.List;

/**
 * User: soldier
 * Date: 06.06.12
 * Time: 17:05
 */
public class JavaScriptTest extends BaseTest {

    @Test
    public void testQueryUsingWhere() {
        Person person = new Person("Jan", "Kowalski", 18);
        ds.save(person);
        person = new Person("Jan", "Kowalski", 21);
        ds.save(person);

        BasicDBObject query = new BasicDBObject();
        query.append("$where", "function(){return this.firstname === 'Jan';}");
        List<Person> persons = toList(Person.class, createNativeQuery("Person").find(query));
        Assert.assertEquals(2, persons.size());
    }

    @Test
    public void testQueryUsingWhere2() {
        Person person = new Person("Jan", "Kowalski", 18);
        ds.save(person);
        person = new Person("Jan", "Smith", 21);
        ds.save(person);

        BasicDBObject query = new BasicDBObject();
        query.append("$where", "this.firstname === 'Jan'");
        query.append("lastname", "Kowalski");
        List<Person> persons = toList(Person.class, createNativeQuery("Person").find(query));
        Assert.assertEquals(1, persons.size());
    }

}
