package net.kyrptonaught.customportalapi.networking;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.util.Identifier;

public class NetworkManager implements DedicatedServerModInitializer {
    public static final Identifier SYNC_PORTALS = new Identifier(CustomPortalsMod.MOD_ID, "syncportals");
    public static final Identifier PLACE_PORTAL = new Identifier(CustomPortalsMod.MOD_ID, "placeportal");

    @Override
    public void onInitializeServer() {
        PortalRegistrySync.registerSyncOnPlayerJoin();
    }
}