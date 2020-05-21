package space.netbytes.adshunters.services;

import com.sun.istack.Nullable;
import space.netbytes.adshunters.converters.Converter;
import space.netbytes.adshunters.exceptions.ResourceNotFoundException;
import space.netbytes.adshunters.models.UserDetails;
import space.netbytes.adshunters.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserDetailsService {

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserDetailsRepository userRepository;

    public List<UserDetails> getAllUserDetails() {
        return (List<UserDetails>) userRepository.findAll();
    }

    public UserDetails getUserDetails(UUID uuid) throws ResourceNotFoundException {
        if (keycloakService.hasPermission(uuid)) {
            return userRepository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this uuid :: " + uuid));
        }
        return null;
    }

    public boolean userExist() {
        return userRepository.existsById(keycloakService.getUUID());
    }

    public UserDetails createUserDetails() {
        if (!userExist()) {
            UserDetails userDetails = UserDetails.builder().id(keycloakService.getUUID()).build();
            System.out.println(userDetails.getId());
            return userRepository.save(userDetails);
        }
        return null;
    }

    public UserDetails updateUserDetails(UserDetails userDetailsNew) throws ResourceNotFoundException {
        if (keycloakService.hasPermission(userDetailsNew.getId())) {
            UserDetails userDetails = userRepository.findById(userDetailsNew.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Domain vote not found for this uuid : " + userDetailsNew.getId()));
            Converter.copyNotNullProperties(userDetailsNew, userDetails);
            return userRepository.save(userDetails);
        } return null;
    }

    public void deleteUserDetails(@Nullable UUID uuid) throws ResourceNotFoundException {
        if (keycloakService.hasPermission(uuid)) {
            UserDetails userDetails = userRepository.findById(uuid)
                    .orElseThrow(() -> new ResourceNotFoundException("Vote not found for this uuid :: " + uuid));
        }
    }
}
