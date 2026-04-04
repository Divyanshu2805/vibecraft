package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.auth.UserProfileResponse;
import com.java.vibecraft.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }
}
