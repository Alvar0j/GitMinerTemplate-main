package aiss.gitminer.controller;

import aiss.gitminer.repository.CommitRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class CommitController {
    private final CommitRepository repository;
    public CommitController(CommitRepository repository){
        this.repository = repository;
    }
}
