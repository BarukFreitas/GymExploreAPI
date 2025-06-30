package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.GymCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.GymDTO;
import br.com.dbc.vemser.GymExploreAPI.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @PostMapping
    public ResponseEntity<GymDTO> create(@RequestBody GymCreateDTO gymCreateDTO) {
        return new ResponseEntity<>(gymService.create(gymCreateDTO), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<GymDTO>> list() {
        return new ResponseEntity<>(gymService.list(), HttpStatus.OK);
    }

    @PutMapping("/{gymId}")
    public ResponseEntity<GymDTO> update(@PathVariable Integer gymId,
                                         @RequestBody GymCreateDTO gymUpdateDTO) {
        GymDTO updatedGym = gymService.update(gymId, gymUpdateDTO);
        return new ResponseEntity<>(updatedGym, HttpStatus.OK);
    }

    @DeleteMapping("/{gymId}")
    public ResponseEntity<Void> delete(@PathVariable Integer gymId) {
        gymService.delete(gymId);
        return ResponseEntity.noContent().build();
    }
}