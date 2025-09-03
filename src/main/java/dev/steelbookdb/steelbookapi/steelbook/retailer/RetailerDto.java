package dev.steelbookdb.steelbookapi.steelbook.retailer;

import lombok.Builder;

@Builder
public record RetailerDto(
    Long id,
    String name,
    String website
) {

}
