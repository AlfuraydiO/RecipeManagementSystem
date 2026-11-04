package org.omar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Optional;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.omar.recipemanagementsystem.RecipesApplication;
import org.omar.recipemanagementsystem.recipe.controller.TagRepository;
import org.omar.recipemanagementsystem.recipe.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RecipesApplication.class)
class CacheTest {

  @Autowired
  TagRepository tagRepository;

  @Value("classpath:ehcache.xml")
  Resource resourceFile;

  @BeforeEach
  void setUp() {
    tagRepository.save(new Tag(null, "Test tag", "Testing tag cache", "TEST"));
    tagRepository.save(new Tag(null, "Test tag 1", "Testing tag cache 2", "TEST"));
  }

  private Optional<Tag> getCachedTag(String name) throws IOException {
    CachingProvider provider = Caching.getCachingProvider();
    CacheManager cacheManager = provider.getCacheManager(resourceFile.getURI(), getClass().getClassLoader());
    Cache<String, Tag> cache = cacheManager.getCache("tagCache", String.class, Tag.class);
    return Optional.ofNullable(cache.get(name));
  }

  @Test
  void cachesTagByName() throws IOException {
    Optional<Tag> tag = tagRepository.findByName("Test tag");

    assertTrue(tag.isPresent());
    Optional<Tag> cachedTag = getCachedTag("Test tag");
    assertTrue(cachedTag.isPresent());
    assertEquals(tag.get().getName(), cachedTag.get().getName());
    assertEquals(tag.get().getDescription(), cachedTag.get().getDescription());
  }
}
