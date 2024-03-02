package uz.pdp.dto;


public record UserSignUpDto (
        String username,
        String email,
        String password,
        String gender) {


}