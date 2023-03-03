package io.github.tox1cozz.forgenetworkfix.asm.mixin;

import io.github.tox1cozz.forgenetworkfix.TargetableMessageCodec;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SimpleNetworkWrapper.class, remap = false)
public class MixinSimpleNetworkWrapper {

    @Shadow
    @Final
    private SimpleIndexedCodec packetCodec;

    @SuppressWarnings("unchecked")
    @Inject(method = "registerMessage(Lnet/minecraftforge/fml/common/network/simpleimpl/IMessageHandler;Ljava/lang/Class;ILnet/minecraftforge/fml/relauncher/Side;)V",
            at = @At(value = "HEAD"))
    private <REQ extends IMessage, REPLY extends IMessage> void registerMessage(IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> requestMessageType, int discriminator, Side side, CallbackInfo callback) {
        ((TargetableMessageCodec<IMessage>)packetCodec).setTargetSide(requestMessageType, side);
    }
}