/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.network;

import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.server.network.NetHandlerStatusServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class NetHandlerHandshakeTCP
implements INetHandlerHandshakeServer {
    private final MinecraftServer server;
    private final NetworkManager networkManager;

    public NetHandlerHandshakeTCP(MinecraftServer serverIn, NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }

    @Override
    public void processHandshake(C00Handshake packetIn) {
        switch (1.$SwitchMap$net$minecraft$network$EnumConnectionState[packetIn.getRequestedState().ordinal()]) {
            case 1: {
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
                if (packetIn.getProtocolVersion() > 47) {
                    ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.9");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
                    this.networkManager.closeChannel(chatcomponenttext);
                    return;
                }
                if (packetIn.getProtocolVersion() < 47) {
                    ChatComponentText chatcomponenttext1 = new ChatComponentText("Outdated client! Please use 1.8.9");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext1));
                    this.networkManager.closeChannel(chatcomponenttext1);
                    return;
                }
                this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
                return;
            }
            case 2: {
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
                return;
            }
        }
        throw new UnsupportedOperationException("Invalid intention " + (Object)((Object)packetIn.getRequestedState()));
    }

    @Override
    public void onDisconnect(IChatComponent reason) {
    }
}

