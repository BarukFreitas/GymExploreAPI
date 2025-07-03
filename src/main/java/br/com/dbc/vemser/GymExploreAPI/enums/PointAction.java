package br.com.dbc.vemser.GymExploreAPI.enums;

import lombok.Getter;

@Getter
public enum PointAction {
    CREATE_POST(10),
    CREATE_REVIEW(15);

    private final int points;

    PointAction(int points) {
        this.points = points;
    }
}