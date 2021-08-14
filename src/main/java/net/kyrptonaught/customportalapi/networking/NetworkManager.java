package net.kyrptonaught.customportalapi.networking;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class NetworkManager implements DedicatedServerModInitializer {
    public static final Identifier SYNC_PORTALS = new Identifier(CustomPortalsMod.MOD_ID, "syncportals");
    public static final Identifier SYNC_SETTINGS = new Identifier(CustomPortalsMod.MOD_ID, "syncsettings");
    private static boolean serverSideOnlyMode = false;

    public static boolean doesPlayerHaveMod(ServerPlayerEntity player) {
        return ServerPlayNetworking.canSend(player, SYNC_PORTALS);
    }

    public static void setServerSideOnlyMode(Boolean serverSideOnlyMode){
        NetworkManager.serverSideOnlyMode = serverSideOnlyMode;
    }
    public static boolean isServerSideOnlyMode() {
        return serverSideOnlyMode;
    }

    @Override
    public void onInitializeServer() {
        PortalRegistrySync.registerSyncOnPlayerJoin();
    }
}
