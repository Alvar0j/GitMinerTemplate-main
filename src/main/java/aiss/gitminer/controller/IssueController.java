package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {
    @Autowired
    IssueRepository issueRepository;

    //GET http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Retrive a list of Issues",
            description = "Get a list of Issues",
            tags = {"Issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Issue.class))})
    })
    @GetMapping
    public List<Issue> findAll(){
        return issueRepository.findAll();
    }

    //GET http://localhost:8080/gitminer/issues/{id}
    @Operation(
            summary = "Retrive a issue by Id",
            description = "Get a Issue object by specifying its id",
            tags = {"issues", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Issue.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @GetMapping("/{id}")
    public Issue findOne(@Parameter(description = "id of the issue to be searched") @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue= issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    //GET http://localhost:8080/gitminer/issues/1554713335/comments
    @GetMapping("/{id}/comments")
    public List<Comment> issueComments(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> optionalIssue = issueRepository.findById(id);
        if (!optionalIssue.isPresent()) {
            throw new IssueNotFoundException();
        }
        Issue issue = optionalIssue.get();
        List<Comment> comments = issue.getComments();
        return comments;
    }

    //POST http://localhost:8080/gitminer/issues
    @Operation(
            summary = "Insert a Issue",
            description = "Add a new Issue whose data is passed in the body of the request in JSON format",
            tags = {"issues", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Issue.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
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
    @Operation(
            summary = "Delete a Issue",
            description = "Delete a Issue object by specifying its id",
            tags = {"issues", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if (issueRepository.existsById(id)){
            issueRepository.deleteById(id);
        }
    }

}
