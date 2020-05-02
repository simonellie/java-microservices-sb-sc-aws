package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {
    @Autowired
    ItemRepository itemRepository;

    @Override
    public List<Item> SelByDescrizione(String descrizione) {
        return itemRepository.findByDescrizioneLike(descrizione);
    }

    @Override
    public Item SelByCodArt(String codArt) {
        return itemRepository.findByCodArt(codArt);
    }

    @Override
    public Item SelByBarcode(String barcode) {
        return itemRepository.SelByEan(barcode);
    }

    @Override
    @Transactional
    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }

    @Override
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
}
