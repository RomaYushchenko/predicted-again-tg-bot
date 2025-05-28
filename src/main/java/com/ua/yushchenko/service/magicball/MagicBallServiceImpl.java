package com.ua.yushchenko.service.magicball;

import com.ua.yushchenko.service.client.ChatGptServiceClient;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MagicBallServiceImpl implements MagicBallService {

    @NonNull
    private final ChatGptServiceClient chatGptServiceClient;

    @Override
    public String getMagicBallResponse(final String userQuestion) {
        return chatGptServiceClient.askMagicBall(userQuestion);
    }
}
