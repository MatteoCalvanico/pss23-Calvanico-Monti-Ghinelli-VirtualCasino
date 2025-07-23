package it.unibo.viewTest;

import org.testfx.api.FxToolkit;
import org.testfx.util.WaitForAsyncUtils;
import it.unibo.virtualCasino.controller.singleton.PlayerHolder;
import it.unibo.virtualCasino.model.scoreboard.Scoreboard;

public final class TestUtils {

    private TestUtils() {
    }

    public static void cleanAfterFxTest() throws Exception {
        // 1) chiude tutti gli Stage aperti
        FxToolkit.cleanupStages();
        WaitForAsyncUtils.waitForFxEvents();

        // 2) reset del singleton PlayerHolder
        PlayerHolder.getInstance().setPlayerHolded(null);

        // 3) scoreboard "in-mem" + file (se usi storage locale)
        Scoreboard.clear();
        Scoreboard.deleteStorageFile();
    }
}
