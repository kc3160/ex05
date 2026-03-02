package com.ufund.api.ufundapi.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdminTests {
    private Admin admin;

    @BeforeEach
    void testsetUp() {
        admin = new Admin("Name", "email@email.com", "securepassword");
    }

    @Test
    void testConstructor() {
        assertThat(admin.getName()).isEqualTo("Name");
        assertThat(admin.getEmail()).isEqualTo("email@email.com");
        assertThat(admin.getPassword()).isEqualTo("securepassword");
    }

    @Test
    void testSetName() {
        admin.setName("New Name");
        assertThat(admin.getName()).isEqualTo("New Name");
    }

    @Test
    void testSetEmail() {
        admin.setEmail("newemail@email.com");
        assertThat(admin.getEmail()).isEqualTo("newemail@email.com");
    }

    @Test
    void testSetPassword() {
        admin.setPassword("newsecurepassword");
        assertThat(admin.getPassword()).isEqualTo("newsecurepassword");
    }

    @Test
    void testEqualsSameObject() {
        assertThat(admin).isEqualTo(admin);
    }

    @Test
    void testEqualsDifferentObjectSameValues() {
        Admin anotherAdmin = new Admin("Name", "email@email.com", "securepassword");
        assertThat(admin).isEqualTo(anotherAdmin);
    }

    @Test
    void testEqualsDifferentName() {
        Admin differentAdmin = new Admin("New Name", "email@email.com", "securepassword");
        assertThat(admin).isNotEqualTo(differentAdmin);
    }

    @Test
    void testEqualsDifferentEmail() {
        Admin differentAdmin = new Admin("Name", "newemail@email.com", "securepassword");
        assertThat(admin).isNotEqualTo(differentAdmin);
    }

    @Test
    void testEqualsDifferentPassword() {
        Admin differentAdmin = new Admin("Name", "email@email.com", "differentpassword");
        assertThat(admin).isNotEqualTo(differentAdmin);
    }

    @Test
    void testEqualsNullObject() {
        assertThat(admin).isNotEqualTo(null);
    }

    @Test
    void testEqualsDifferentClass() {
        assertThat(admin).isNotEqualTo("Not a Admin");
    }

    @Test
    void testHashCodeSameEmail() {
        Admin anotherAdmin = new Admin("Name", "email@email.com", "securepassword");
        assertThat(admin.hashCode()).isEqualTo(anotherAdmin.hashCode());
    }

    @Test
    void testHashCodeDifferentEmail() {
        Admin differentAdmin = new Admin("Name", "newemail@email.com", "securepassword");
        assertThat(admin.hashCode()).isNotEqualTo(differentAdmin.hashCode());
    }
}