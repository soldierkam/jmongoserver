package org.kamionowski.jmongoserver.tests;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kamionowski.jmongoserver.tests.dao.BlogEntryDao;
import org.kamionowski.jmongoserver.tests.domain.Author;
import org.kamionowski.jmongoserver.tests.domain.BlogEntry;

/**
 * User: soldier
 * Date: 12.06.12
 * Time: 19:23
 */
public class QueringEmbededObjectsTest extends BaseTest {

    private BlogEntryDao blogEntryDao;

    @Before
    public void initDao() {
        blogEntryDao = new BlogEntryDao(mongo, morphia);
    }

    @Test
    public void testQueryUsingWhere() {


        Author author = new Author("jsmith", "www.jsmith.com");
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setAuthor(author);
        blogEntry.setContent("content text");
        blogEntry.setLead("lead text");
        blogEntry.setTitle("first entry");
        blogEntryDao.save(blogEntry);

        Author author2 = new Author("jsmith2", "www.jsmith.com");
        blogEntry = new BlogEntry();
        blogEntry.setAuthor(author2);
        blogEntry.setContent("content2 text");
        blogEntry.setLead("lead2 text");
        blogEntry.setTitle("second entry");
        blogEntryDao.save(blogEntry);

        com.google.code.morphia.query.Query<BlogEntry> query = blogEntryDao.createQuery();
        Assert.assertEquals(1, query.filter("author.name", "jsmith2").countAll());
        query = blogEntryDao.createQuery();
        Assert.assertEquals(1, query.filter("author", author).countAll());
    }
}
