/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn
extends CommandBase {
    @Override
    public String getCommandName() {
        return "save-on";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.save-on.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        boolean flag = false;
        int i = 0;
        while (true) {
            if (i >= minecraftserver.worldServers.length) {
                if (!flag) throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
                CommandSaveOn.notifyOperators(sender, (ICommand)this, "commands.save.enabled", new Object[0]);
                return;
            }
            if (minecraftserver.worldServers[i] != null) {
                WorldServer worldserver = minecraftserver.worldServers[i];
                if (worldserver.disableLevelSaving) {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
            ++i;
        }
    }
}

