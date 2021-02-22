package com.leijendary.spring.microservicetemplate.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public abstract class AppCache<T> {

    private final CacheManager cacheManager;

    private Cache cache;

    @PostConstruct
    public void init() {
        this.cache = cacheManager.getCache(cacheName());
    }

    public abstract Class<T> type();

    public String cacheName() {
        return type().getSimpleName();
    }

    protected String splitter() {
        return "::";
    }

    public T get(Object key) {
        return this.cache.get(key, type());
    }

    @Async
    public void put(Object key, T value) {
        this.cache.put(key, value);
    }

    @Async
    public void evict(Object key) {
        this.cache.evict(key);
    }

    @Async
    public void clear() {
        this.cache.clear();
    }
}
