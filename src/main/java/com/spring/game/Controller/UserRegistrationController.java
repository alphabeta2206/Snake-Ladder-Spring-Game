package com.spring.game.Controller;

import com.spring.game.dto.UserRegistrationDTO;
import com.spring.game.model.User;
import com.spring.game.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class UserRegistrationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationController.class);
    @Autowired
    private UserService userService;

    @Operation(summary = "Get All Currency codes and Multipliers")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "User Registered Successfully", content = {@Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "404", description = "Incorrect Details or User Already Exists", content = @Content)})
    @RequestMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO user_details){
        String username = user_details.getUsername();
        if(userService.findByUsername(username)!=null){
            return ResponseEntity.status(HttpStatus.IM_USED).body("User Already Exists, Register with different Details");
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.saveUser(user_details));
    }
}
