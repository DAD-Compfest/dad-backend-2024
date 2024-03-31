package com.dadcompfest.backend.modules.authmodule.service;

public abstract class UserService<T> implements ICrudService<T>, IAuthService<T> {
   public void updatePassword(String username, String newPassword){}
}

