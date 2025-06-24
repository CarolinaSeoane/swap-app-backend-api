package com.swapapp.swapappmockserver.controller;

import com.swapapp.swapappmockserver.model.Kiosk;
import com.swapapp.swapappmockserver.repository.kiosk.IKioskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kiosks")
public class KioskController {

    @Autowired
    private IKioskRepository kioskRepository;

    @GetMapping
    public List<Kiosk> getKiosks() {
        return kioskRepository.getKiosks();
    }
}
