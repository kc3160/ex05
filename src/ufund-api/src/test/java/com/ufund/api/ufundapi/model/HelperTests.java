package com.ufund.api.ufundapi.model;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelperTests {
    private Helper helper;

    @BeforeEach
    void testSetUp() {
        helper = new Helper("Name", "email@email.com", "securepassword", 1);
    }

    @Test
    void testConstructor() {
        assertThat(helper.getName()).isEqualTo("Name");
        assertThat(helper.getEmail()).isEqualTo("email@email.com");
        assertThat(helper.getPassword()).isEqualTo("securepassword");
        assertThat(helper.getBasketId()).isEqualTo(1);
    }

    @Test
    void testSetName() {
        helper.setName("New Name");
        assertThat(helper.getName()).isEqualTo("New Name");
    }

    @Test
    void testSetEmail() {
        helper.setEmail("newemail@email.com");
        assertThat(helper.getEmail()).isEqualTo("newemail@email.com");
    }

    @Test
    void testSetPassword() {
        helper.setPassword("newsecurepassword");
        assertThat(helper.getPassword()).isEqualTo("newsecurepassword");
    }

    @Test
    void testSetBasketId() {
        helper.setBasketId(2);
        assertThat(helper.getBasketId()).isEqualTo(2);
    }

    @Test
    void testEqualsSameObject() {
        assertThat(helper).isEqualTo(helper);
    }

    @Test
    void testEqualsDifferentObjectSameValues() {
        Helper anotherHelper = new Helper("Name", "email@email.com", "securepassword", 1);
        assertThat(helper).isEqualTo(anotherHelper);
    }

    @Test
    void testEqualsDifferentName() {
        Helper differentHelper = new Helper("New Name", "email@email.com", "securepassword", 1);
        assertThat(helper).isNotEqualTo(differentHelper);
    }

    @Test
    void testEqualsDifferentEmail() {
        Helper differentHelper = new Helper("Name", "newemail@email.com", "securepassword", 1);
        assertThat(helper).isNotEqualTo(differentHelper);
    }

    @Test
    void testEqualsDifferentPassword() {
        Helper differentHelper = new Helper("Name", "email@email.com", "differentpassword", 1);
        assertThat(helper).isNotEqualTo(differentHelper);
    }

    @Test
    void testEqualsDifferentBasketId() {
        Helper differentHelper = new Helper("Name", "email@email.com", "securepassword", 2);
        assertThat(helper).isEqualTo(differentHelper);
    }

    @Test
    void testEqualsNullObject() {
        assertThat(helper).isNotEqualTo(null);
    }

    @Test
    void testEqualsDifferentClass() {
        assertThat(helper).isNotEqualTo("Not a Helper");
    }

    @Test
    void testHashCodeSameEmail() {
        Helper anotherHelper = new Helper("Name", "email@email.com", "securepassword", 1);
        assertThat(helper.hashCode()).isEqualTo(anotherHelper.hashCode());
    }

    @Test
    void testHashCodeDifferentEmail() {
        Helper anotherHelper = new Helper("Name", "newemail@email.com", "securepassword", 1);
        assertThat(helper.hashCode()).isNotEqualTo(anotherHelper.hashCode());
    }
}