
package org.omar;

import java.io.IOException;
import java.util.Iterator;
 
import java.util.Optional;
import javax.cache.Cache;
import javax.cache.Cache.Entry;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omar.recipes.RecipesApplication;
import org.omar.recipes.recipe.controller.TagRepository;
import org.omar.recipes.recipe.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

/**
 *
 * @author oalfuraydi
 */
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = RecipesApplication.class)
public class CacheTest {

    @Autowired
    TagRepository tagRepository;
    
     @Value("classpath:ehcache.xml")
    Resource resourcefile;
 
    @BeforeEach
    void setUp() {
        tagRepository.save(new Tag(null, "TEst tag", "testing tag caache", "TEST"));
        tagRepository.save(new Tag(null, "TEst tag 1", "testing tag caache 2", "TEST"));
    }

    private Optional<Tag> getCachedTag(String name) throws IOException {

        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = null;
        cacheManager = provider.getCacheManager(resourcefile.getURI(), getClass().getClassLoader());
        Iterable<String> cacheNames = cacheManager.getCacheNames();
        cacheNames.forEach(System.out::println);
        Cache<String, Tag> cache = cacheManager.getCache("tagCache", String.class, Tag.class);
         for (Iterator iterator = cache.iterator(); iterator.hasNext();) {
            Entry<String,Tag> next =  (Entry<String,Tag>) iterator.next();
            if (next.getValue().getName().equals(name));
            return Optional.of(next.getValue());
        }
        return Optional.empty();
    }

    @Test
    void TestCacheTag() throws IOException {
        Optional<Tag> testTag = tagRepository.findByName("TEst tag");
        System.err.println("Testing....");
        assertEquals(testTag, getCachedTag("TEst tag"));
    }

}
