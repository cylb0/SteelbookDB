package dev.steelbookdb.steelbookapi.steelbook;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RetailerMapperTest {

    private RetailerMapper retailerMapper = new RetailerMapper();

    @Test
    void toDto_CorrectlyMaps_GivenRetailerEntity() {
        Retailer retailer = new Retailer();
        retailer.setId(1L);
        retailer.setName("Test Retailer");
        retailer.setWebsite("https://testretailer.com");

        RetailerDto dto = retailerMapper.toDto(retailer);

        assertNotNull(dto);
        assert retailer.getId().equals(dto.id());
        assert retailer.getName().equals(dto.name());
        assert retailer.getWebsite().equals(dto.website());
    }

    @Test
    void toDto_ReturnsNull_GivenNullRetailer() {
        RetailerDto dto = retailerMapper.toDto(null);
        assertNull(dto);
    }
}
