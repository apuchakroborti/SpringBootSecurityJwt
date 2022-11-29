package com.apu.example.springsecurityjwt.specifications;

import com.apu.example.springsecurityjwt.entity.CustomUser;
import com.apu.example.springsecurityjwt.util.Utils;
import org.springframework.data.jpa.domain.Specification;

public class CustomUserSearchSpecifications {
    public static Specification<CustomUser> withId(Long id){
        return (root, query, cb) -> id != null ? cb.equal(root.get("id"), id) : cb.conjunction();
    }
    public static Specification<CustomUser> withFirstName(String firstName){
        return (root, query, cb) -> !Utils.isNullOrEmpty(firstName) ?
                cb.like(root.get("firstName"), "%" + firstName + "%") :
                cb.conjunction();
    }
    public static Specification<CustomUser> withLastName(String lastName){
        return (root, query, cb) -> !Utils.isNullOrEmpty(lastName) ?
                cb.like(root.get("lastName"), "%" + lastName + "%") :
                cb.conjunction();
    }
    public static Specification<CustomUser> withEmail(String email){
        return (root, query, cb) -> !Utils.isNullOrEmpty(email) ?
                cb.equal(root.get("email"), email) :
                cb.conjunction();
    }
}
