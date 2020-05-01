package com.xantrix.webapp.service;

import com.xantrix.webapp.entity.Item;

import java.util.List;

public interface ItemService {
    List<Item> SelByDescrizione(String descrizione);
    Item SelByCodArt(String codArt);
    Item SelByBarcode(String barcode);
    void deleteItem(Item item);
    void insertItem(Item item);
}
