package dev.steelbookdb.steelbookapi.steelbook.steelbook;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SteelbookService {

    private final SteelbookMapper steelbookMapper;
    private final SteelbookRepository steelbookRepository;
}
