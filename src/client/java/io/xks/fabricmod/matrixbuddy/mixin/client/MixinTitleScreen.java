package io.xks.fabricmod.matrixbuddy.mixin.client;

import io.xks.fabricmod.matrixbuddy.eventbus.EventBus;
import io.xks.fabricmod.matrixbuddy.eventbus.events.TitleScreenEntryEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class MixinTitleScreen extends Screen {
    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void TitlesScreenEntry(CallbackInfo info) {
        EventBus.publish(new TitleScreenEntryEvent());
//        assert this.client != null;
//        this.client.setScreen(new SelectWorldScreen(this));

//        List<LevelStorage.LevelSave> list = this.client.getLevelStorage().getLevelList().levels();
//        list.get(0).
//        if (this.client.getLevelStorage().levelExists(this.level.getName())) {
//            this.openReadingWorldScreen();
//            this.client.createIntegratedServerLoader().start(this.screen, this.level.getName());
//        }
    }
}
