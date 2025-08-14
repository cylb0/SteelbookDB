package dev.steelbookdb.steelbookapi.movie.director;

import org.springframework.web.bind.annotation.RestController;

import dev.steelbookdb.steelbookapi.movie.director.dto.CreateDirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.DirectorDto;
import dev.steelbookdb.steelbookapi.movie.director.dto.UpdateDirectorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping("/directors")
    public DirectorDto saveDirector(@Valid @RequestBody CreateDirectorDto dto) {
        return directorService.createDirector(dto);
    }

    @GetMapping("/directors")
    public List<DirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/directors/{id}")
    public DirectorDto getDirectorById(@PathVariable Long id) {
        return directorService.getDirectorById(id);
    }

    @PatchMapping("/directors/{id}")
    public DirectorDto updateDirector(@PathVariable Long id, @RequestBody UpdateDirectorDto dto) {
        return directorService.updateDirector(id, dto);
    }

    @DeleteMapping("/directors/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDirector(@PathVariable Long id) {
        directorService.deleteDirector(id);
    }
}
