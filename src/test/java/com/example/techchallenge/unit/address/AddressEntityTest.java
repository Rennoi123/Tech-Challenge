package com.example.techchallenge.unit.address;

import com.example.techchallenge.core.domain.entities.Address;
import com.example.techchallenge.infrastructure.entities.AddressEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressEntityTest {

    @Test
    void deveConverterDeDomainParaEntity() {
        Address domain = new Address(1L, "Rua A", "123", "Ap 1",
                "Centro", "Cidade X", "SP", "12345-000");

        AddressEntity entity = AddressEntity.fromDomain(domain);

        assertEquals(domain.getId(), entity.getId());
        assertEquals("Rua A", entity.getStreet());
    }

    @Test
    void deveConverterDeEntityParaDomain() {
        AddressEntity entity = new AddressEntity();
        entity.setId(2L);
        entity.setStreet("Rua B");
        entity.setNumber("456");

        Address domain = entity.toDomain();

        assertEquals(entity.getId(), domain.getId());
        assertEquals("Rua B", domain.getStreet());
        assertEquals("456", domain.getNumber());
    }

    @Test
    void devePermitirCamposNulosNaConversao() {
        AddressEntity entity = new AddressEntity();
        Address domain = entity.toDomain();
        assertNull(domain.getStreet());
    }
}
