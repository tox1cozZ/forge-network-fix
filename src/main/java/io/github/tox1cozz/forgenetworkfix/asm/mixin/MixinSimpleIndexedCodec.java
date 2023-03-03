package io.github.tox1cozz.forgenetworkfix.asm.mixin;

import cpw.mods.fml.common.network.simpleimpl.SimpleIndexedCodec;
import cpw.mods.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = SimpleIndexedCodec.class, remap = false)
public class MixinSimpleIndexedCodec<A> extends MixinFMLIndexedMessageToMessageCodec<A> {

    @Override
    public boolean canDecode(Class<? extends A> type, Side side) {
        byte messageSide = targetSides.get(type);
        return (messageSide & toMask(side)) != 0;
    }
}