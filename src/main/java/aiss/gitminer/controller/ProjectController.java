package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
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

@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    // GET http://localhost:8080/api/projects
    @Operation(
            summary = "Retrive a list of projects",
            description = "Get a list of projects",
            tags = {"projects", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Project.class))})
    })
    @GetMapping
    /*public List<Project> findAll() {

        return projectRepository.findAll();
    }*/
    public List<Project> findAll(@RequestParam(required = false) String id,
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

        Page<Project> pageProjects;

        if (id == null)
            pageProjects = projectRepository.findAll(paging);
        else
            pageProjects = projectRepository.findById(id, paging);

        return  pageProjects.getContent();

    }

    // GET http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Retrive a project by Id",
            description = "Get a project object by specifying its id",
            tags = {"projects", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @GetMapping("/{id}")
    public Project findOne(@Parameter(description = "id of the comment to be searched") @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project= projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    // POST http://localhost:8080/gitminer/projects
    @Operation(
            summary = "Insert a Project",
            description = "Add a new Project whose data is passed in the body of the request in JSON format",
            tags = {"projects", "post"})
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(schema = @Schema(implementation = Project.class))}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody @Valid Project project) {
        Project project1=projectRepository.save(new Project(project.getId(),project.getName(),
                project.getWebUrl(),project.getCommits(),project.getIssues()));
        return project1;
    }


    //PUT http://localhost:8080/gitminer/projects/{id}
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProject(@RequestBody @Valid Project updatedProject, @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> projectData= projectRepository.findById(id);
        if(!projectData.isPresent()){
            throw new ProjectNotFoundException();
        }
        Project _project=  projectData.get();
        _project.setName(updatedProject.getName());
        _project.setWebUrl(updatedProject.getWebUrl());
        projectRepository.save(_project);

    }

    //DELETE http://localhost:8080/gitminer/projects/{id}
    @Operation(
            summary = "Delete a Project",
            description = "Delete a Project object by specifying its id",
            tags = {"projects", "delete"})
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "400", content = {@Content(schema = @Schema())}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})

    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(@PathVariable String  id) {
        if (projectRepository.existsById(id)){
            projectRepository.deleteById(id);
        }
    }

}
