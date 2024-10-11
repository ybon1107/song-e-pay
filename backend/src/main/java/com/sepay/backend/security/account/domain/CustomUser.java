package com.sepay.backend.security.account.domain;

<<<<<<< HEAD
=======
import com.sepay.backend.user.dto.UserDTO;
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUser extends User {
    private UserVO userVO;

<<<<<<< HEAD
    public CustomUser(String username, String password,
                      Collection<? extends GrantedAuthority> authorities) {
=======
    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
>>>>>>> 59aa309e4cbc61504bfe42cdb43cc5b4b7d664ff
        super(username, password, authorities);
    }

    public CustomUser(UserVO userVO) {
        super(userVO.getUsername(), userVO.getPassword(), userVO.getAuthList());
        this.userVO = userVO;
    }
}
