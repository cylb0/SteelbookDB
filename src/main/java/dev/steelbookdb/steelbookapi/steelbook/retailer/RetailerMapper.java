package dev.steelbookdb.steelbookapi.steelbook.retailer;

import org.springframework.stereotype.Service;

@Service
public class RetailerMapper {

    public RetailerDto toDto(Retailer retailer) {
        if (retailer == null) return null;
        return new RetailerDto(
            retailer.getId(),
            retailer.getName(),
            retailer.getWebsite()
        );
    }
}
