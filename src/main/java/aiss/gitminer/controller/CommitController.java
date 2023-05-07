package aiss.gitminer.controller;

import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {
    @Autowired
    CommitRepository commitRepository;

    //GET http://localhost:8080/gitminer/commits
    @GetMapping
    public List<Commit> findAll(){
        return commitRepository.findAll();
    }

    //GET http://localhost:8080/gitminer/commits/{id}
    @GetMapping("/{id}")
    public Commit findOne (@PathVariable String id){
        Optional<Commit> commit = commitRepository.findById(id);
        return commit.get();
    }

    //POST http://localhost:8080/gitminer/commits
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id){
        if (commitRepository.existsById(id)){
            commitRepository.deleteById(id);
        }
    }



}
