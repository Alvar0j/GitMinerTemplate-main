package aiss.gitminer.controller;

import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {
    @Autowired
    ProjectRepository projectRepository;

    //GET http://localhost:8080/gitminer/projects
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    /*     @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project= projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }*/
    //GET http://localhost:8080/gitminer/projects/{id}
    @GetMapping("/{id}")
    public Project findOne(@PathVariable long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.get();
    }

    //POST http://localhost:8080/gitminer/projects
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project create(@Valid @RequestBody Project project){
        Project _project= projectRepository.
                save(new Project(
                        project.getId(),
                        project.getName(),
                        project.getWebUrl(),
                        project.getCommits(),
                        project.getIssues()));
        return _project;
    }
    /*
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
    }*/

    //DELETE http://localhost:8080/gitminer/projects/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id){
        if (projectRepository.existsById(id)){
            projectRepository.deleteById(id);
        }
    }
}
