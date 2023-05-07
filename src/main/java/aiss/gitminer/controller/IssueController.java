package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
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
    public Issue findOne(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Issue> issue= issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new ProjectNotFoundException();
        }
        return issue.get();
    }

    //GET http://localhost:8080/gitminer/issues/1554713335/comments
    @GetMapping("/{id}/comments")
    public List<Comment> getIssueComments(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        if (!optionalIssue.isPresent()) {
            throw new ProjectNotFoundException();
        }
        Issue issue = optionalIssue.get();
        List<Comment> comments = issue.getComments();
        return comments;
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
    public void delete(@PathVariable String id){
        if (issueRepository.existsById(id)){
            issueRepository.deleteById(id);
        }
    }

}
