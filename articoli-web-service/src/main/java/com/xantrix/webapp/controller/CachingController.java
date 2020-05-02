package com.xantrix.webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cache")
@Api(value = "alphashop", tags = "Controller for caching")
public class CachingController {
    @Autowired
    CacheManager cacheManager;

    @ApiOperation(
            value = "Clean \"items\" and \"item\" caches",
            notes = "Return message for deleted caches",
            response = ObjectNode.class,
            produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cache deleted"),
            @ApiResponse(code = 403, message = "No authorization"),
            @ApiResponse(code = 401, message = "No authentication")
    })
    @GetMapping("/clearAllCaches")
    public ResponseEntity<?> clearAllCaches() {
        cacheManager.getCache("items").clear();
        cacheManager.getCache("item").clear();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();

        responseNode.put("code", HttpStatus.OK.value());
        responseNode.put("message", "Cache \"items\" and \"item\" cleaned!");
        return new ResponseEntity<>(responseNode, new HttpHeaders(), HttpStatus.OK);
    }
}
