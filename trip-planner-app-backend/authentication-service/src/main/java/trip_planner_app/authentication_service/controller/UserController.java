package trip_planner_app.authentication_service.controller;

import org.springframework.http.ResponseEntity;
import trip_planner_app.authentication_service.dto.UserDTO;
import trip_planner_app.authentication_service.dto.UserResponseDTO;
import trip_planner_app.authentication_service.model.User;
import trip_planner_app.authentication_service.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/trip-planner/authentication")
@Api(tags = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @PostMapping("/signin")
    @ApiOperation(value = "${UserController.signin}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public ResponseEntity<?> login(@ApiParam("Username") @RequestParam String username,
                                   @ApiParam("Password") @RequestParam String password,
                                   HttpServletResponse response) {
        String token = userService.signin(username, password);

        // JWT token'ı `Cookie` olarak ekle
        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600); // 1 saat geçerli

        response.addCookie(cookie);

        return ResponseEntity.ok(token);
    }

    /*
    @PostMapping("/signin")
public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password, HttpServletResponse response) {
    String token = userService.signin(username, password);

    // JWT token'ı Cookie olarak ekle
    Cookie cookie = new Cookie("JWT_TOKEN", token);
    cookie.setHttpOnly(true); // JavaScript erişimini engellemek için
    cookie.setSecure(true); // HTTPS üzerinden erişimi zorunlu hale getirir
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60); // 7 gün boyunca geçerli

    response.addCookie(cookie);

    return ResponseEntity.ok("User signed in successfully!");
}

     */
    @PostMapping("/signup")
    @ApiOperation(value = "${UserController.signup}")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 422, message = "Username is already in use")})
    public String signup(@ApiParam("Signup User") @RequestBody UserDTO user) {
        return userService.signup(modelMapper.map(user, User.class));
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.delete}", authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public String delete(@ApiParam("Username") @PathVariable String username) {
        userService.delete(username);
        return username;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "${UserController.search}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 404, message = "The user doesn't exist"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
        return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    @ApiOperation(value = "${UserController.me}", response = UserResponseDTO.class, authorizations = { @Authorization(value="apiKey") })
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = "Something went wrong"), //
            @ApiResponse(code = 403, message = "Access denied"), //
            @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
    public UserResponseDTO whoami(HttpServletRequest req) {
        return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public String refresh(HttpServletRequest req) {
        return userService.refresh(req.getRemoteUser());
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello from AuthService";
    }

}