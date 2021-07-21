package one.digitalinnovation.bookstock.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BookGenre {

    HORROR("Horror"),
    SCIFI("SciFi"),
    ROMANCE("Romance"),
    DETECTIVE("Detective"),
    FANTASY("Fantasy"),
    ADVENTURE("Adventure"),
    BIOGRAPHY("Biography"),
    POETRY("Poetry"),
    HISTORIC("Historic"),
    ACADEMIC("Academic");

    private final String description;

}