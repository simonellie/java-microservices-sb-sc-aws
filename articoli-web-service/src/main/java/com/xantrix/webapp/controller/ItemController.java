package com.xantrix.webapp.controller;

import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.exception.instance.NotFoundException;
import com.xantrix.webapp.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("api/items")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @GetMapping(value = "/search/ean/{barcode}")
    public ResponseEntity<Item> listItemsByEan(@PathVariable("barcode") String barcode) throws NotFoundException {
        Item item = itemService.SelByBarcode(barcode);
        if (item == null) {
            String errorMessage = String.format("Item with barcode %s not found.", barcode);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping(value = "/search/code/{codArt}")
    public ResponseEntity<Item> listItemsByCodArt(@PathVariable("codArt") String codArt) throws NotFoundException {
        Item item = itemService.SelByCodArt(codArt);
        if (item == null) {
            String errorMessage = String.format("Item with code %s not found.", codArt);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @GetMapping(value = "/search/description/{description}")
    public ResponseEntity<List<Item>> listItemsByDescription(@PathVariable("description") String description) throws NotFoundException {
        List<Item> items = itemService.SelByDescrizione("%" + description + "%");
        if (items == null || items.isEmpty()) {
            String errorMessage = String.format("Items with description %s not found.", description);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

}
