package org.kamionowski.jmongoserver.tests;

import com.mysema.query.mongodb.morphia.MorphiaQuery;
import org.junit.Assert;
import org.junit.Test;
import org.kamionowski.jmongoserver.tests.domain.Person;
import org.kamionowski.jmongoserver.tests.domain.QPerson;

/**
 * User: soldier
 * Date: 05.06.12
 * Time: 19:40
 */
public class InsertFlatObjectTest extends BaseTest {

    @Test
    public void testDocumentWithOneField() {
        Person person = new Person("Jan", "Kowalski", 18);
        ds.save(person);
        Assert.assertNotNull(person.getId());
        Assert.assertEquals(1, ds.createQuery(Person.class).countAll());
    }

    @Test
    public void testDocumentWithOneFieldGt() {
        Person person = new Person("Jan", "Kowalski", 18);
        ds.save(person);
        person = new Person("Jan", "Kowalski", 21);
        ds.save(person);
        Assert.assertNotNull(person.getId());
        MorphiaQuery<Person> query = createQuery(QPerson.person);
        Assert.assertEquals(1, query.where(QPerson.person.age.gt(18)).count());
    }


}
