package br.com.dbc.vemser.GymExploreAPI.controller;

import br.com.dbc.vemser.GymExploreAPI.dto.GymCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.GymDTO;
import br.com.dbc.vemser.GymExploreAPI.service.GymService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/gyms")
@RequiredArgsConstructor
public class GymController {

    private final GymService gymService;

    @PostMapping
    public ResponseEntity<GymDTO> create(@RequestBody GymCreateDTO gymCreateDTO) {
        return new ResponseEntity<>(gymService.create(gymCreateDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GymDTO>> list() {
        return new ResponseEntity<>(gymService.list(), HttpStatus.OK);
    }

    @PutMapping("/{gymId}/image")
    public ResponseEntity<String> uploadImage(@PathVariable Integer gymId,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        String message = gymService.addImage(gymId, file);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/{gymId}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer gymId) {
        Gym gym = gymService.findById(gymId);

        if (gym.getImageData() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(gym.getImageMimeType()));
        headers.setContentLength(gym.getImageData().length);

        return new ResponseEntity<>(gym.getImageData(), headers, HttpStatus.OK);
    }
}