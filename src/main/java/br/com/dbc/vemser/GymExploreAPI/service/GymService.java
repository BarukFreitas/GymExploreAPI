package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.GymCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.dto.GymDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.Gym;
import br.com.dbc.vemser.GymExploreAPI.repository.GymRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
    public List<GymDTO> list() {
        return gymRepository.findAll()
                .stream()
                .map(gym -> objectMapper.convertValue(gym, GymDTO.class))
                .collect(Collectors.toList());
    }

    public GymDTO update(Integer gymId, GymCreateDTO gymUpdateDTO) {
        Gym gym = gymRepository.findById(gymId)
                .orElseThrow(() -> new EntityNotFoundException("Academia não encontrada com o id: " + gymId));

        gym.setName(gymUpdateDTO.getName());
        gym.setAddress(gymUpdateDTO.getAddress());
        gym.setPhone(gymUpdateDTO.getPhone());
        gym.setImageUrl(gymUpdateDTO.getImageUrl());

        Gym updatedGym = gymRepository.save(gym);

        return objectMapper.convertValue(updatedGym, GymDTO.class);
    }

    public void delete(Integer gymId) {
        if (!gymRepository.existsById(gymId)) {
            throw new EntityNotFoundException("Academia não encontrada com o id: " + gymId);
        }
        gymRepository.deleteById(gymId);
    }
}