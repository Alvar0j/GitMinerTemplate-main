package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
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

@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {
    @Autowired
    CommentRepository repository;

    //GET http://localhost:8080/gitminer/comments
    @Operation(
            summary = "Retrive a list of comments",
            description = "Get a list of comments",
            tags = {"comments", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Comment.class))})
    })
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }


    //GET http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Retrive a comment by Id",
            description = "Get a Comment object by specifying its id",
            tags = {"comments", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @GetMapping("/{id}")
    public Comment findOne(@Parameter(description = "id of the comment to be searched") @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

    //POST http://localhost:8080/gitminer/comments
    @Operation(
            summary = "Insert a Comment",
            description = "Add a new Comment whose data is passed in the body of the request in JSON format",
            tags = {"comments", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Comment.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Comment create(@Valid @RequestBody Comment comment){
        Comment _comment = repository.
                save(new Comment(comment.getId(),comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(), comment.getAuthor()));
        return _comment;
    }

    //El PUT no hace falta
    //PUT http://localhost:8080/gitminer/comments/{id}
    /*@PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update (@Valid @RequestBody Comment updateComment, @PathVariable long id){
        Optional<Comment> commentData = repository.findById(id);

        Comment _comment = commentData.get();
        _comment.setAuthor(updateComment.getAuthor());
        _comment.setCreatedAt(updateComment.getCreatedAt());
        _comment.setUpdatedAt(updateComment.getUpdatedAt());
        _comment.setAuthor(updateComment.getAuthor());
        repository.save(_comment);
    }

     */

    //DELETE http://localhost:8080/gitminer/comments/{id}
    @Operation(
            summary = "Delete a Comment",
            description = "Delete a Comment object by specifying its id",
            tags = {"comments", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@Parameter(description = "id of the comment to be deleted") @PathVariable String id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
    }


}
