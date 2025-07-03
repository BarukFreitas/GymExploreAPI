package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.dto.StoreItemCreateDTO;
import br.com.dbc.vemser.GymExploreAPI.entity.StoreItem;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.entity.UserPurchase;
import br.com.dbc.vemser.GymExploreAPI.exception.RegraDeNegocioException;
import br.com.dbc.vemser.GymExploreAPI.repository.StoreItemRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserPurchaseRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreItemRepository storeItemRepository;
    private final UserPurchaseRepository userPurchaseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;


    public List<StoreItem> listAllItems() {
        return storeItemRepository.findAll();
    }


    @Transactional
    public void purchaseItem(Long userId, Integer itemId) throws RegraDeNegocioException {
        // Busca as entidades necessárias do banco de dados
        UserEntity user = userService.findById(userId);
        StoreItem item = storeItemRepository.findById(itemId)
                .orElseThrow(() -> new RegraDeNegocioException("Item não encontrado na loja."));


        if (user.getPoints() < item.getPointsCost()) {
            throw new RegraDeNegocioException("Pontos insuficientes para trocar por este item.");
        }


        user.setPoints(user.getPoints() - item.getPointsCost());
        userRepository.save(user);


        UserPurchase purchase = new UserPurchase(user, item);
        userPurchaseRepository.save(purchase);
    }
    public StoreItem createItem(StoreItemCreateDTO itemCreateDTO) {
        StoreItem item = objectMapper.convertValue(itemCreateDTO, StoreItem.class);
        return storeItemRepository.save(item);
    }
}