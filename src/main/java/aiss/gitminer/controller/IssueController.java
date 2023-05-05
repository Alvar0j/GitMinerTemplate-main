package aiss.gitminer.controller;

import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {
    @Autowired
    IssueRepository issueRepository;

    //GET http://localhost:8080/gitminer/issues
    @GetMapping
    public List<Issue> findAll(){
        return issueRepository.findAll();
    }

    //GET http://localhost:8080/gitminer/issues/{id}
    @GetMapping("/{id}")
    public Issue findOne (@PathVariable long id){
        Optional<Issue> issue = issueRepository.findById(id);
        return issue.get();
    }

    //POST http://localhost:8080/gitminer/issues
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Issue create (@Valid @RequestBody Issue issue){
        Issue _issue = issueRepository.
                save((new Issue(issue.getId(), issue.getRefId(), issue.getTitle(),
                        issue.getDescription(), issue.getState(),
                        issue.getCreatedAt(), issue.getUpdatedAt(),
                        issue.getClosedAt(), issue.getLabels(),
                        issue.getAuthor(), issue.getAssignee(),
                        issue.getUpvotes(), issue.getDownvotes(),
                        issue.getWebUrl(), issue.getComments())));
        return _issue;

    }

    //DELETE http://localhost:8080/gitminer/issues/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        if (issueRepository.existsById(id)){
            issueRepository.deleteById(id);
        }
    }

}
