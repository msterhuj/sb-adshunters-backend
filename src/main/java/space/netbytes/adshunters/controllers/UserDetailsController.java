package space.netbytes.adshunters.controllers;

import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.UserDetails;
import space.netbytes.adshunters.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserDetailsController {

    @Autowired
    private UserDetailsService userService;

    @GetMapping
    public List<UserDetails> getAllUserDetails() {
        return userService.getAllUserDetails();
    }

    @GetMapping({"/{userId}", "/details"})
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public UserDetails getUserDetails(@PathVariable(value = "userId") UUID userId) throws ResourceNotFoundException {
        return userService.getUserDetails(userId);
        // todo return 401 if null
    }

    @PostMapping
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public UserDetails createUserDetails() {
        return userService.createUserDetails();
        // todo return 401 if null
    }

    @PutMapping
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public UserDetails updateUserDetails(@RequestBody UserDetails userDetails) throws ResourceNotFoundException {
        return userService.updateUserDetails(userDetails);
        // todo return 401 if null
    }

    @DeleteMapping({"/{userId}", "/"})
    @PreAuthorize("hasRole('ADSHUNTERS_USER')")
    public void deleteUserDetails(@PathVariable(value = "userId") UUID userId) throws ResourceNotFoundException {
        userService.deleteUserDetails(userId);
    }
}
