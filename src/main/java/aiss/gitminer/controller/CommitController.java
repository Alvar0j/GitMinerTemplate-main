package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.CommitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {
    @Autowired
    CommitRepository commitRepository;

    //GET http://localhost:8080/gitminer/commits
    @Operation(
            summary = "Retrive a list of commits",
            description = "Get a list of commits",
            tags = {"commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class))})
    })
    @GetMapping
    /*public List<Commit> findAll(){

        return commitRepository.findAll();
    }*/
    public List<Commit> findAll(@RequestParam(required = false) String id,
                                 @RequestParam(required = false) String order,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size
    ){
        Pageable paging;

        if (order != null){
            if (order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        }
        else
            paging = PageRequest.of(page, size);

        Page<Commit> pageProjects;

        if (id == null)
            pageProjects = commitRepository.findAll(paging);
        else
            pageProjects = commitRepository.findById(id, paging);

        return  pageProjects.getContent();

    }

    //GET http://localhost:8080/gitminer/commits/{id}
    @Operation(
            summary = "Retrive a commit by Id",
            description = "Get a Commit object by specifying its id",
            tags = {"commit", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Commit.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @GetMapping("/{id}")
    public Commit findOne (@Parameter(description = "id of the commit to be searched") @PathVariable String id) throws CommitNotFoundException{
        Optional<Commit> commit = commitRepository.findById(id);
        if (!commit.isPresent()) {
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

    //POST http://localhost:8080/gitminer/commits
    @Operation(
            summary = "Insert a Commit",
            description = "Add a new Commit whose data is passed in the body of the request in JSON format",
            tags = {"commits", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Commit.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Commit create (@Valid @RequestBody Commit commit){
        Commit _commit = commitRepository.
                save((new Commit(commit.getId(), commit.getTitle(),
                        commit.getMessage(), commit.getAuthorName(),
                        commit.getAuthorEmail(), commit.getAuthoredDate(),
                        commit.getCommitterName(), commit.getCommitterEmail(),
                        commit.getCommittedDate(), commit.getWebUrl())));
        return _commit;

    }

    //DELETE http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Delete a Commit",
            description = "Delete a Commit object by specifying its id",
            tags = {"commits", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if (commitRepository.existsById(id)){
            commitRepository.deleteById(id);
        }
    }



}
