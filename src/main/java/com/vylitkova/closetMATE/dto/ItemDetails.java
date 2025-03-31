package com.vylitkova.closetMATE.dto;

import com.vylitkova.closetMATE.entity.item.Item;
import com.vylitkova.closetMATE.entity.item.ItemService;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemDetails {
    private UUID id;
    private boolean favourite;
    private String image;

    private String type;
    private String category;

    private Set<String> colors;
    private String color_warmness;
    private String color_darkness;

    private String pattern;
    private String material;

    private String season;
    private Integer temperature_min;
    private Integer temperature_max;

    private String formality;
    private String style;
    private String mood;
    private String purpose;
    private String public_url;
    private LocalDateTime url_expiry_date;

}
