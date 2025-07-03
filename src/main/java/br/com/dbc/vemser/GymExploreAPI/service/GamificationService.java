package br.com.dbc.vemser.GymExploreAPI.service;

import br.com.dbc.vemser.GymExploreAPI.entity.GamificationLog;
import br.com.dbc.vemser.GymExploreAPI.entity.UserEntity;
import br.com.dbc.vemser.GymExploreAPI.enums.PointAction;
import br.com.dbc.vemser.GymExploreAPI.repository.GamificationLogRepository;
import br.com.dbc.vemser.GymExploreAPI.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class GamificationService {

    private final UserRepository userRepository;
    private final GamificationLogRepository gamificationLogRepository;

    public boolean awardPoints(UserEntity user, PointAction action) {
        if (user == null || action == null) {
            return false;
        }

        LocalDate today = LocalDate.now();

        long dailyActionCount = gamificationLogRepository.countByUserIdAndActionAndActionDate(
                user.getId(),
                action,
                today
        );

        boolean canAwardPoints = false;
        switch (action) {
            case CREATE_REVIEW:
                if (dailyActionCount < 1) {
                    canAwardPoints = true;
                }
                break;
            case CREATE_POST:
                if (dailyActionCount < 3) {
                    canAwardPoints = true;
                }
                break;
        }

        if (canAwardPoints) {
            log.info("A atribuir {} pontos ao utilizador {} pela ação {}. (Ação diária nº {})",
                    action.getPoints(), user.getUsername(), action, dailyActionCount + 1);

            int currentPoints = user.getPoints() != null ? user.getPoints() : 0;
            user.setPoints(currentPoints + action.getPoints());
            userRepository.save(user);

            GamificationLog logEntry = new GamificationLog(user, action);
            gamificationLogRepository.save(logEntry);

            return true;
        } else {
            log.info("Limite diário de pontos para a ação {} já atingido pelo utilizador {}.", action, user.getUsername());
            return false;
        }
    }
}