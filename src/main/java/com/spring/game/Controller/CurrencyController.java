package com.spring.game.Controller;

import com.spring.game.model.CurrencyExchangeRate;
import com.spring.game.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @Operation(summary = "Adds New Currency Code and Multiplier")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Added Currency", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = CurrencyExchangeRate.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/add")
    @ResponseBody
    public ResponseEntity<CurrencyExchangeRate> add(@RequestBody CurrencyExchangeRate currencyExchangeRate){
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.saveCurrency(currencyExchangeRate));
    }

    @Operation(summary = "Get All Currency codes and Multipliers")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Returned Currency List", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = List.class))}),
            @ApiResponse(responseCode = "404", description = "Error", content = @Content)})
    @RequestMapping("/all")
    @ResponseBody
    public ResponseEntity<List<CurrencyExchangeRate>> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(currencyService.getCurrencyExchanges());
    }
}
//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
