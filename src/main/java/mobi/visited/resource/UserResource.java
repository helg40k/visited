package mobi.visited.resource;

import javassist.NotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

import mobi.visited.resource.dto.UserDTO;
import mobi.visited.service.UserService;

@Controller
@RequestMapping("/user")
public class UserResource {

    private final static Logger LOG = LoggerFactory.getLogger(UserResource.class);

    private UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public List<UserDTO> getAll() {
        LOG.info("GetAll");
        return userService.getUsers();
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public UserDTO get(@PathVariable Long userId) throws NotFoundException {
        LOG.info("Get");
        Optional<UserDTO> user = userService.getUserForResource(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The user is not found: ID " + userId);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public Long save(@RequestBody UserDTO userDTO) {
        LOG.info("Save");
        return userService.registerNewUser(userDTO);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT, consumes = "application/json;charset=UTF-8")
    @ResponseBody
    public UserDTO update(@PathVariable Long userId,
                          @RequestBody UserDTO userDTO) throws NotFoundException {
        LOG.info("Update");
        Optional<UserDTO> user = userService.editUser(userId, userDTO);
        if (user.isPresent()) {
            return user.get();
        }
        throw new NotFoundException("The user is not found: ID " + userId);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE, consumes = "application/json;charset=UTF-8")
    public ResponseEntity delete(@PathVariable Long userId) {
        LOG.info("Delete");
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

}
