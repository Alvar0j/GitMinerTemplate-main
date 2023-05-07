package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {
    @Autowired
    CommentRepository repository;

    //GET http://localhost:8080/gitminer/comments
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }
    //GET http://localhost:8080/gitminer/comments/{id}

    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id){
        Optional<Comment> comment = repository.findById(id);
        return comment.get();
    }

    //POST http://localhost:8080/gitminer/comments
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
    }


}
