package com.adpro.backend.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.adpro.backend.type.UserType;

public class AdminTest {
    private Admin admin;
    @BeforeEach
    public void setUp() {
        admin = new Admin("myusername", "mypassword", "myemail@gmail.com");
    }
    @Test 
    public void getAdminType(){
        assertEquals("ADMIN", UserType.ADMIN.getUserType());
    }

    @Test
    public void isExistAdminType(){
        assertTrue(UserType.contains(UserType.ADMIN.getUserType()));;
    }
    @Test
    public void testCreateCustomer() {
        assertNotNull(admin);
        assertEquals("myusername", admin.getUsername());
        assertEquals("mypassword", admin.getPassword());
        assertEquals("myemail@gmail.com", admin.getEmail());
    }

    @Test
    public void testAuthenticateSuccess() {
        assertTrue(admin.authenticate("mypassword"));
    }

    @Test
    public void testAuthenticateFailure() {
        assertFalse(admin.authenticate("wrongpassword"));
    }
}
