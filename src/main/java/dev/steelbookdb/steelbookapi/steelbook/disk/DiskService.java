package dev.steelbookdb.steelbookapi.steelbook.disk;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiskService {

    private final DiskMapper diskMapper;
    private final DiskRepository diskRepository;
    
}
