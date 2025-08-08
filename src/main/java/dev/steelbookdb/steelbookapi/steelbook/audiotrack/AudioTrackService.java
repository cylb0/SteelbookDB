package dev.steelbookdb.steelbookapi.steelbook.audiotrack;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AudioTrackService {

    private final AudioTrackMapper audioTrackMapper;
    private final AudioTrackRepository audioTrackRepository;

}
