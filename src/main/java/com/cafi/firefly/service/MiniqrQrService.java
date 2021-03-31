package com.cafi.firefly.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;

public interface MiniqrQrService {
    String postToken() throws Exception;
    BufferedInputStream getMiniqrQr(String sceneStr, String accessToken);
}
