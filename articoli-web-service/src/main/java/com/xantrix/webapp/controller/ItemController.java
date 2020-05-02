package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.exception.instance.BindingException;
import com.xantrix.webapp.exception.instance.DuplicateException;
import com.xantrix.webapp.exception.instance.NotFoundException;
import com.xantrix.webapp.service.ItemService;
import org.hibernate.cfg.NotYetImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;


@RestController
@RequestMapping("api/item")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    ResourceBundleMessageSource resourceBundleMessageSource;

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

    @PostMapping(value = "/add")
    public ResponseEntity<Item> addItem (@Valid @RequestBody Item item, BindingResult bindingResult /* for validation */) throws BindingException, DuplicateException {
        if (bindingResult.hasErrors() && bindingResult.getFieldError() != null) {
            String errorMessage = resourceBundleMessageSource.getMessage(bindingResult.getFieldError(), new Locale("en"));
            logger.warn(errorMessage);
            throw new BindingException(errorMessage);
        }

        Item findItem = itemService.SelByCodArt(item.getCodArt());
        if (findItem != null) {
            String errorMessage = String.format("Add for item %s already available! It's impossible to use a POST method.", findItem.getCodArt());
            logger.warn(errorMessage);
            throw new DuplicateException(errorMessage);
        }

        itemService.saveItem(item);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.CREATED);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<Item> editItem (@Valid @RequestBody Item item, BindingResult bindingResult /* for validation */) throws BindingException, NotFoundException {
        if (bindingResult.hasErrors() && bindingResult.getFieldError() != null) {
            String errorMessage = resourceBundleMessageSource.getMessage(bindingResult.getFieldError(), new Locale("en"));
            logger.warn(errorMessage);
            throw new BindingException(errorMessage);
        }

        Item findItem = itemService.SelByCodArt(item.getCodArt());
        if (findItem == null) {
            String errorMessage = String.format("Item %s not available! It's impossible to use a PUT method.", item.getCodArt());
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        itemService.saveItem(item);
        return new ResponseEntity<>(new HttpHeaders(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{codArt}", produces = "application/json")
    public ResponseEntity<?> deleteItem (@PathVariable String codArt) throws NotFoundException {
        Item findItem = itemService.SelByCodArt(codArt);
        if (findItem == null) {
            String errorMessage = String.format("Item %s not available!", codArt);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        itemService.deleteItem(findItem);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.value());
        responseNode.put("message", "Delete item " + codArt + " execute with success.");
        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }
}
