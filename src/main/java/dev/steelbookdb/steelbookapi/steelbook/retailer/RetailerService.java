package dev.steelbookdb.steelbookapi.steelbook.retailer;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RetailerService {

    private final RetailerMapper retailerMapper;

}
