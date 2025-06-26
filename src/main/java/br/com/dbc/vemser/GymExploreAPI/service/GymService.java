package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.GymCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.GymDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import br.com.dbc.vemser.GymExploreAPI.repository.GymRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
@Service
@RequiredArgsConstructor
public class GymService {

    private final GymRepository gymRepository;
    private final ObjectMapper objectMapper;

    public GymDTO create(GymCreateDTO gymCreateDTO) {
        Gym gymEntity = objectMapper.convertValue(gymCreateDTO, Gym.class);
        Gym savedGym = gymRepository.save(gymEntity);
        return objectMapper.convertValue(savedGym, GymDTO.class);
    }

    public List<GymDTO> list() {
        return gymRepository.findAll()
                .stream()
                .map(gym -> objectMapper.convertValue(gym, GymDTO.class))
                .collect(Collectors.toList());
    }

    public String addImage(Integer gymId, MultipartFile file) throws IOException {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada com o id: " + gymId));

        gym.setImageData(file.getBytes());
        gym.setImageMimeType(file.getContentType());

        gymRepository.save(gym);
        return "Imagem adicionada com sucesso para a academia: " + gym.getName();
    }

    public Gym findById(Integer gymId) {
        return gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada com o id: " + gymId));
    }
}