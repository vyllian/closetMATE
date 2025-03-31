package com.vylitkova.closetMATE.entity.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ImageInfo {

    private String name;
    private String publicUrl;
    private LocalDateTime expirationDate;

}
