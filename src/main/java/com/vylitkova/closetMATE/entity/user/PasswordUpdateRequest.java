package com.vylitkova.closetMATE.entity.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordUpdateRequest {
    private String oldPassword;
    private String newPassword;
}
