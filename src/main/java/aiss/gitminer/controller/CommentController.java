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
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentRepository repository;
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }
    @GetMapping
    public Comment findOne(@PathVariable long id){
        Optional<Comment> comment = repository.findById(id);
        return comment.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Comment create(@Valid @RequestBody Comment comment){
        Comment _comment = repository.
                save(new Comment(comment.getBody(), comment.getCreatedAt(), comment.getUpdatedAt(), comment.getAuthor()));
        return _comment;
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping
    public void update (@Valid @RequestBody Comment updateComment, @PathVariable long id){
        Optional<Comment> commentData = repository.findById(id);

        Comment _comment = commentData.get();
        _comment.setAuthor(updateComment.getAuthor());
        _comment.setCreatedAt(updateComment.getCreatedAt());
        _comment.setUpdatedAt(updateComment.getUpdatedAt());
        _comment.setAuthor(updateComment.getAuthor());
        repository.save(_comment);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }
    }


}
