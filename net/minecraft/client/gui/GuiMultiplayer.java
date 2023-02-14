/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.input.Keyboard
 */
package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenAddServer;
import net.minecraft.client.gui.GuiScreenServerList;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.client.gui.ServerListEntryLanDetected;
import net.minecraft.client.gui.ServerListEntryLanScan;
import net.minecraft.client.gui.ServerListEntryNormal;
import net.minecraft.client.gui.ServerSelectionList;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.network.LanServerDetector;
import net.minecraft.client.network.OldServerPinger;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiMultiplayer
extends GuiScreen
implements GuiYesNoCallback {
    private static final Logger logger = LogManager.getLogger();
    private final OldServerPinger oldServerPinger = new OldServerPinger();
    private GuiScreen parentScreen;
    private ServerSelectionList serverListSelector;
    private ServerList savedServerList;
    private GuiButton btnEditServer;
    private GuiButton btnSelectServer;
    private GuiButton btnDeleteServer;
    private boolean deletingServer;
    private boolean addingServer;
    private boolean editingServer;
    private boolean directConnect;
    private String hoveringText;
    private ServerData selectedServer;
    private LanServerDetector.LanServerList lanServerList;
    private LanServerDetector.ThreadLanServerFind lanServerDetector;
    private boolean initialized;

    public GuiMultiplayer(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents((boolean)true);
        this.buttonList.clear();
        if (!this.initialized) {
            this.initialized = true;
            this.savedServerList = new ServerList(this.mc);
            this.savedServerList.loadServerList();
            this.lanServerList = new LanServerDetector.LanServerList();
            try {
                this.lanServerDetector = new LanServerDetector.ThreadLanServerFind(this.lanServerList);
                this.lanServerDetector.start();
            }
            catch (Exception exception) {
                logger.warn("Unable to start LAN server detection: " + exception.getMessage());
            }
            this.serverListSelector = new ServerSelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.serverListSelector.func_148195_a(this.savedServerList);
        } else {
            this.serverListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        }
        this.createButtons();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.serverListSelector.handleMouseInput();
    }

    public void createButtons() {
        this.btnEditServer = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, I18n.format("selectServer.edit", new Object[0]));
        this.buttonList.add(this.btnEditServer);
        this.btnDeleteServer = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, I18n.format("selectServer.delete", new Object[0]));
        this.buttonList.add(this.btnDeleteServer);
        this.btnSelectServer = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, I18n.format("selectServer.select", new Object[0]));
        this.buttonList.add(this.btnSelectServer);
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, I18n.format("selectServer.direct", new Object[0])));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.format("selectServer.add", new Object[0])));
        this.buttonList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, I18n.format("selectServer.refresh", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.format("gui.cancel", new Object[0])));
        this.selectServer(this.serverListSelector.func_148193_k());
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (this.lanServerList.getWasUpdated()) {
            List<LanServerDetector.LanServer> list = this.lanServerList.getLanServers();
            this.lanServerList.setWasNotUpdated();
            this.serverListSelector.func_148194_a(list);
        }
        this.oldServerPinger.pingPendingNetworks();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents((boolean)false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.oldServerPinger.clearPendingNetworks();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry;
        if (!button.enabled) return;
        GuiListExtended.IGuiListEntry iGuiListEntry = guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (button.id == 2 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            String s4 = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData().serverName;
            if (s4 == null) return;
            this.deletingServer = true;
            String s = I18n.format("selectServer.deleteQuestion", new Object[0]);
            String s1 = "'" + s4 + "' " + I18n.format("selectServer.deleteWarning", new Object[0]);
            String s2 = I18n.format("selectServer.deleteButton", new Object[0]);
            String s3 = I18n.format("gui.cancel", new Object[0]);
            GuiYesNo guiyesno = new GuiYesNo(this, s, s1, s2, s3, this.serverListSelector.func_148193_k());
            this.mc.displayGuiScreen(guiyesno);
            return;
        }
        if (button.id == 1) {
            this.connectToSelected();
            return;
        }
        if (button.id == 4) {
            this.directConnect = true;
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
            this.mc.displayGuiScreen(new GuiScreenServerList(this, this.selectedServer));
            return;
        }
        if (button.id == 3) {
            this.addingServer = true;
            this.selectedServer = new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "", false);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            return;
        }
        if (button.id == 7 && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            this.editingServer = true;
            ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
            this.selectedServer = new ServerData(serverdata.serverName, serverdata.serverIP, false);
            this.selectedServer.copyFrom(serverdata);
            this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.selectedServer));
            return;
        }
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }
        if (button.id != 8) return;
        this.refreshServerList();
    }

    private void refreshServerList() {
        this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry;
        GuiListExtended.IGuiListEntry iGuiListEntry = guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (this.deletingServer) {
            this.deletingServer = false;
            if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
                this.savedServerList.removeServerData(this.serverListSelector.func_148193_k());
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
            return;
        }
        if (this.directConnect) {
            this.directConnect = false;
            if (result) {
                this.connectToServer(this.selectedServer);
                return;
            }
            this.mc.displayGuiScreen(this);
            return;
        }
        if (this.addingServer) {
            this.addingServer = false;
            if (result) {
                this.savedServerList.addServerData(this.selectedServer);
                this.savedServerList.saveServerList();
                this.serverListSelector.setSelectedSlotIndex(-1);
                this.serverListSelector.func_148195_a(this.savedServerList);
            }
            this.mc.displayGuiScreen(this);
            return;
        }
        if (!this.editingServer) return;
        this.editingServer = false;
        if (result && guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            ServerData serverdata = ((ServerListEntryNormal)guilistextended$iguilistentry).getServerData();
            serverdata.serverName = this.selectedServer.serverName;
            serverdata.serverIP = this.selectedServer.serverIP;
            serverdata.copyFrom(this.selectedServer);
            this.savedServerList.saveServerList();
            this.serverListSelector.func_148195_a(this.savedServerList);
        }
        this.mc.displayGuiScreen(this);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry;
        int i = this.serverListSelector.func_148193_k();
        GuiListExtended.IGuiListEntry iGuiListEntry = guilistextended$iguilistentry = i < 0 ? null : this.serverListSelector.getListEntry(i);
        if (keyCode == 63) {
            this.refreshServerList();
            return;
        }
        if (i < 0) {
            super.keyTyped(typedChar, keyCode);
            return;
        }
        if (keyCode == 200) {
            if (GuiMultiplayer.isShiftKeyDown()) {
                if (i <= 0) return;
                if (!(guilistextended$iguilistentry instanceof ServerListEntryNormal)) return;
                this.savedServerList.swapServers(i, i - 1);
                this.selectServer(this.serverListSelector.func_148193_k() - 1);
                this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                this.serverListSelector.func_148195_a(this.savedServerList);
                return;
            }
            if (i <= 0) {
                this.selectServer(-1);
                return;
            }
            this.selectServer(this.serverListSelector.func_148193_k() - 1);
            this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
            if (!(this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)) return;
            if (this.serverListSelector.func_148193_k() > 0) {
                this.selectServer(this.serverListSelector.getSize() - 1);
                this.serverListSelector.scrollBy(-this.serverListSelector.getSlotHeight());
                return;
            }
            this.selectServer(-1);
            return;
        }
        if (keyCode == 208) {
            if (GuiMultiplayer.isShiftKeyDown()) {
                if (i >= this.savedServerList.countServers() - 1) return;
                this.savedServerList.swapServers(i, i + 1);
                this.selectServer(i + 1);
                this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                this.serverListSelector.func_148195_a(this.savedServerList);
                return;
            }
            if (i >= this.serverListSelector.getSize()) {
                this.selectServer(-1);
                return;
            }
            this.selectServer(this.serverListSelector.func_148193_k() + 1);
            this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
            if (!(this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k()) instanceof ServerListEntryLanScan)) return;
            if (this.serverListSelector.func_148193_k() < this.serverListSelector.getSize() - 1) {
                this.selectServer(this.serverListSelector.getSize() + 1);
                this.serverListSelector.scrollBy(this.serverListSelector.getSlotHeight());
                return;
            }
            this.selectServer(-1);
            return;
        }
        if (keyCode != 28 && keyCode != 156) {
            super.keyTyped(typedChar, keyCode);
            return;
        }
        this.actionPerformed((GuiButton)this.buttonList.get(2));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.hoveringText = null;
        this.drawDefaultBackground();
        this.serverListSelector.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.title", new Object[0]), this.width / 2, 20, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (this.hoveringText == null) return;
        this.drawHoveringText(Lists.newArrayList(Splitter.on("\n").split(this.hoveringText)), mouseX, mouseY);
    }

    public void connectToSelected() {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry;
        GuiListExtended.IGuiListEntry iGuiListEntry = guilistextended$iguilistentry = this.serverListSelector.func_148193_k() < 0 ? null : this.serverListSelector.getListEntry(this.serverListSelector.func_148193_k());
        if (guilistextended$iguilistentry instanceof ServerListEntryNormal) {
            this.connectToServer(((ServerListEntryNormal)guilistextended$iguilistentry).getServerData());
            return;
        }
        if (!(guilistextended$iguilistentry instanceof ServerListEntryLanDetected)) return;
        LanServerDetector.LanServer lanserverdetector$lanserver = ((ServerListEntryLanDetected)guilistextended$iguilistentry).getLanServer();
        this.connectToServer(new ServerData(lanserverdetector$lanserver.getServerMotd(), lanserverdetector$lanserver.getServerIpPort(), true));
    }

    private void connectToServer(ServerData server) {
        this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, server));
    }

    public void selectServer(int index) {
        this.serverListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.serverListSelector.getListEntry(index);
        this.btnSelectServer.enabled = false;
        this.btnEditServer.enabled = false;
        this.btnDeleteServer.enabled = false;
        if (guilistextended$iguilistentry == null) return;
        if (guilistextended$iguilistentry instanceof ServerListEntryLanScan) return;
        this.btnSelectServer.enabled = true;
        if (!(guilistextended$iguilistentry instanceof ServerListEntryNormal)) return;
        this.btnEditServer.enabled = true;
        this.btnDeleteServer.enabled = true;
    }

    public OldServerPinger getOldServerPinger() {
        return this.oldServerPinger;
    }

    public void setHoveringText(String p_146793_1_) {
        this.hoveringText = p_146793_1_;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.serverListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.serverListSelector.mouseReleased(mouseX, mouseY, state);
    }

    public ServerList getServerList() {
        return this.savedServerList;
    }

    public boolean func_175392_a(ServerListEntryNormal p_175392_1_, int p_175392_2_) {
        if (p_175392_2_ <= 0) return false;
        return true;
    }

    public boolean func_175394_b(ServerListEntryNormal p_175394_1_, int p_175394_2_) {
        if (p_175394_2_ >= this.savedServerList.countServers() - 1) return false;
        return true;
    }

    public void func_175391_a(ServerListEntryNormal p_175391_1_, int p_175391_2_, boolean p_175391_3_) {
        int i = p_175391_3_ ? 0 : p_175391_2_ - 1;
        this.savedServerList.swapServers(p_175391_2_, i);
        if (this.serverListSelector.func_148193_k() == p_175391_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.func_148195_a(this.savedServerList);
    }

    public void func_175393_b(ServerListEntryNormal p_175393_1_, int p_175393_2_, boolean p_175393_3_) {
        int i = p_175393_3_ ? this.savedServerList.countServers() - 1 : p_175393_2_ + 1;
        this.savedServerList.swapServers(p_175393_2_, i);
        if (this.serverListSelector.func_148193_k() == p_175393_2_) {
            this.selectServer(i);
        }
        this.serverListSelector.func_148195_a(this.savedServerList);
    }
}

