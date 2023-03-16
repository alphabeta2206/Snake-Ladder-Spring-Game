package com.Task1.Task.controller;

import com.Task1.Task.model.CurrencyExchangeRate;
import com.Task1.Task.model.Game;
import com.Task1.Task.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class CurrencyController {
    @Autowired
    CurrencyService currencyService;

    @Operation(summary = "Adds New Currency Code and Multiplier")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Added Currency", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CurrencyExchangeRate.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @PostMapping("/add")
    public ResponseEntity<CurrencyExchangeRate> setCurrency(@RequestBody CurrencyExchangeRate currency) {
        currencyService.setCurrency(currency);
        return ResponseEntity.status(HttpStatus.OK).body(currency);
    }
}
