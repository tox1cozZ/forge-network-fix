package io.github.tox1cozz.forgenetworkfix;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.relauncher.Side;

public interface TargetableMessageCodec<A> {

    void setTargetSide(Class<? extends IMessage> type, Side side);

    boolean canDecode(Class<? extends A> type, Side side);
}