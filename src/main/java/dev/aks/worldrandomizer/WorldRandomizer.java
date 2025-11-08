package dev.aks.worldrandomizer;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Минимальный энтрипоинт. Дальше уже навешиваем слушатели и маппинг.
public class WorldRandomizer implements ModInitializer {
    public static final String MOD_ID = "world_randomizer";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOG.info("[WorldRandomizer] init 1.21 OK");
        // Твою детерминированную таблицу блок-на-блок прикрутим после,
        // как только сборка стабильно пройдет.
    }
}
