package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xantrix.webapp.entity.Item;
import com.xantrix.webapp.exception.instance.BindingException;
import com.xantrix.webapp.exception.instance.DuplicateException;
import com.xantrix.webapp.exception.instance.NotFoundException;
import com.xantrix.webapp.service.ItemService;
import io.swagger.annotations.*;
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
@Api(value = "alphashop", tags = "Controller for item's handling")
public class ItemController {
    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemService itemService;

    @Autowired
    ResourceBundleMessageSource resourceBundleMessageSource;

    @ApiOperation(
            value = "Search item by barcode",
            notes = "Return item data in json format",
            response = Item.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found"),
            @ApiResponse(code = 404, message = "Item not found"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @GetMapping(value = "/search/ean/{barcode}")
    public ResponseEntity<Item> listItemsByEan(
            @ApiParam("Unique barcode for item") @PathVariable("barcode") String barcode
    ) throws NotFoundException {
        Item item = itemService.SelByBarcode(barcode);
        if (item == null) {
            String errorMessage = String.format("Item with barcode %s not found.", barcode);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Search item by item code",
            notes = "Return item data in json format",
            response = Item.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found"),
            @ApiResponse(code = 404, message = "Item not found"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @GetMapping(value = "/search/code/{codArt}")
    public ResponseEntity<Item> listItemsByCodArt(
            @ApiParam("Unique code for item") @PathVariable("codArt") String codArt
    ) throws NotFoundException {
        Item item = itemService.SelByCodArt(codArt);
        if (item == null) {
            String errorMessage = String.format("Item with code %s not found.", codArt);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Search items by item description",
            notes = "Return items in json format",
            response = List.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item found"),
            @ApiResponse(code = 404, message = "Item not found"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @GetMapping(value = "/search/description/{description}")
    public ResponseEntity<List<Item>> listItemsByDescription(
            @ApiParam("Item's description") @PathVariable("description") String description
    ) throws NotFoundException {
        List<Item> items = itemService.SelByDescrizione(description);
        if (items == null || items.isEmpty()) {
            String errorMessage = String.format("Items with description %s not found.", description);
            logger.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Add a new item",
            notes = "Return added item in json format",
            response = Item.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item added"),
            @ApiResponse(code = 400, message = "Received item not well formatted"),
            @ApiResponse(code = 406, message = "Item already added"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @PostMapping(value = "/add")
    public ResponseEntity<Item> addItem (
            @ApiParam("Item to add") @Valid @RequestBody Item item,
            @ApiParam("Validation result for item") BindingResult bindingResult /* for validation */
    ) throws BindingException, DuplicateException {
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

    @ApiOperation(
            value = "Edit an item",
            notes = "Return edited item in json format",
            response = Item.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item edited"),
            @ApiResponse(code = 400, message = "Received item not well formatted"),
            @ApiResponse(code = 404, message = "Item not found"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @PutMapping(value = "/edit")
    public ResponseEntity<Item> editItem (
            @ApiParam("Item to modify") @Valid @RequestBody Item item,
            @ApiParam("Validation result for item") BindingResult bindingResult /* for validation */
    ) throws BindingException, NotFoundException {
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

    @ApiOperation(
            value = "Delete an item",
            notes = "Return message for deleted item",
            response = ObjectNode.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Item deleted"),
            @ApiResponse(code = 404, message = "Item not found"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @DeleteMapping(value = "/delete/{codArt}", produces = "application/json")
    public ResponseEntity<?> deleteItem (
            @ApiParam("Code that identify item to delete") @PathVariable String codArt
    ) throws NotFoundException {
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
