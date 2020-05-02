package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@CacheConfig(cacheNames = {"items"})
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Override
    @Cacheable
    public List<Item> SelByDescrizione(String descrizione) {
        return itemRepository.findByDescrizioneLike("%" + descrizione + "%");
    }

    @Override
    @Cacheable(value = "item", key = "#codArt", sync = true)
    public Item SelByCodArt(String codArt) {
        return itemRepository.findByCodArt(codArt);
    }

    @Override
    @Cacheable
    public Item SelByBarcode(String barcode) {
        return itemRepository.SelByEan(barcode);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "items", allEntries = true),
            @CacheEvict(cacheNames = "item", key = "#item.codArt")
    })
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "items", allEntries = true),
            @CacheEvict(cacheNames = "item", key = "#item.codArt")
    })
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
}
