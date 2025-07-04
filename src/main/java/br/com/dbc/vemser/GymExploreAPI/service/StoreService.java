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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreService {

    private final StoreItemRepository storeItemRepository;
    private final UserPurchaseRepository userPurchaseRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    public List<StoreItem> listAllItems() {
        return storeItemRepository.findAll();
    }

    public StoreItem createItem(StoreItemCreateDTO itemCreateDTO) {
        StoreItem item = objectMapper.convertValue(itemCreateDTO, StoreItem.class);
        return storeItemRepository.save(item);
    }

    @Transactional
    public void purchaseItem(Long userId, Integer itemId) throws RegraDeNegocioException {
        UserEntity user = userService.findById(userId);
        StoreItem item = storeItemRepository.findById(itemId)
                .orElseThrow(() -> new RegraDeNegocioException("Item não encontrado na loja."));

        if (user.getPoints() < item.getPointsCost()) {
            throw new RegraDeNegocioException("Pontos insuficientes para trocar por este item.");
        }

        user.setPoints(user.getPoints() - item.getPointsCost());
        UserEntity updatedUser = userRepository.save(user);

        UserPurchase purchase = new UserPurchase(updatedUser, item);
        userPurchaseRepository.save(purchase);

        try {
            emailService.sendPurchaseConfirmationEmail(updatedUser, item);
        } catch (MessagingException e) {
            log.error("Não foi possível enviar o e-mail de confirmação para o utilizador: " + userId, e);
        }
    }

    @Transactional
    public StoreItem updateItem(Integer itemId, StoreItemCreateDTO itemUpdateDTO) throws RegraDeNegocioException {
        StoreItem item = storeItemRepository.findById(itemId)
                .orElseThrow(() -> new RegraDeNegocioException("Item da loja não encontrado para atualização."));

        item.setName(itemUpdateDTO.getName());
        item.setDescription(itemUpdateDTO.getDescription());
        item.setPointsCost(itemUpdateDTO.getPointsCost());

        return storeItemRepository.save(item);
    }

    @Transactional
    public void deleteItem(Integer itemId) throws RegraDeNegocioException {
        if (!storeItemRepository.existsById(itemId)) {
            throw new RegraDeNegocioException("Item da loja não encontrado para exclusão.");
        }
        storeItemRepository.deleteById(itemId);
    }
}