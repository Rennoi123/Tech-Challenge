package com.example.techchallenge.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    String street;
    String number;
    String complement;
    String neighborhood;
    String city;
    String state;
    String postalCode;
}
