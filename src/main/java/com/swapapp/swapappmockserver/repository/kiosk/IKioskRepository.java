package com.swapapp.swapappmockserver.repository.kiosk;

import com.swapapp.swapappmockserver.model.Kiosk;
import java.util.List;

public interface IKioskRepository {
    List<Kiosk> getKiosks();
}
