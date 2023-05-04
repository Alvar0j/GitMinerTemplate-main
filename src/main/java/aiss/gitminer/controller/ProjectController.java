package aiss.gitminer.controller;

import aiss.gitminer.repository.IssueRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class ProjectController {
    private final IssueRepository repository;
    public ProjectController(IssueRepository repository){
        this.repository = repository;
    }

}
