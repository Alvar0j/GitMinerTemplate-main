package aiss.gitminer.controller;

import aiss.gitminer.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class UserController {
    private final UserRepository repository;
    public UserController(UserRepository repository){
        this.repository = repository;
    }
}
