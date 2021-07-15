package net.kyrptonaught.customportalapi.portal;

import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class FlatPortalAreaHelper {
    private final HashSet<Block> VALID_FRAME;
    protected int foundPortalBlocks = 0;

    public FlatPortalAreaHelper() {
        VALID_FRAME = new HashSet<>();
        VALID_FRAME.add(Blocks.COBBLESTONE);
    }
    public static Optional<FlatPortalAreaHelper> getNewPortal(WorldAccess worldAccess, BlockPos blockPos, Direction.Axis axis, HashSet<Block> foundations) {
        return getOrEmpty(worldAccess, blockPos, (areaHelper) -> {
            return areaHelper.isValid() && areaHelper.foundPortalBlocks == 0;
        }, axis, foundations);
    }
    public HashSet<BlockPos> insidePos = new HashSet<>();

    public boolean detectPortal(World world, BlockPos portalPos) {
        spreadPos(world, portalPos);
        Pair<Integer,Integer> CDs = isValidShape();
        if (CDs.getLeft() < 0 || CDs.getRight() < 0) return false;
        System.out.println(isValidFrame(world));
        return true;
    }

    public void spreadPos(World world, BlockPos portalPos) {
        if (insidePos.size() > 30 || insidePos.contains(portalPos) || !CustomAreaHelper.validStateInsidePortal(world.getBlockState(portalPos), VALID_FRAME))
            return;
        if (CustomPortalsMod.isInstanceOfCustomPortal(world, portalPos))
            foundPortalBlocks++;
        insidePos.add(portalPos);
        spreadPos(world, portalPos.north());
        spreadPos(world, portalPos.east());
        spreadPos(world, portalPos.south());
        spreadPos(world, portalPos.west());
    }

    public Pair<Integer,Integer> isValidShape() {
        HashMap<Integer, Integer> X_Coords = new HashMap<>();
        HashMap<Integer, Integer> Z_Coords = new HashMap<>();
        for (BlockPos pos : insidePos) {
            X_Coords.put(pos.getX(), X_Coords.getOrDefault(pos.getX(), 0) + 1);
            Z_Coords.put(pos.getZ(), Z_Coords.getOrDefault(pos.getZ(), 0) + 1);
        }
        int Xs = -1, Zs = -1;
        for (Integer x : X_Coords.values()) {
            if (Xs == -1) Xs = x;
            if (x != Xs) Xs = -999;
        }
        for (Integer z : Z_Coords.values()) {
            if (Zs == -1) Zs = z;
            if (z != Zs) Zs = -999;
        }
        return new Pair<>(Xs,Zs);
    }

    public boolean isValidFrame(World world) {
        for (BlockPos pos : insidePos) {
            if (!checkFrame(world.getBlockState(pos.north())) ||
                    !checkFrame(world.getBlockState(pos.east())) ||
                    !checkFrame(world.getBlockState(pos.south())) ||
                    !checkFrame(world.getBlockState(pos.west())))
                return false;
        }
        return true;
    }

    private boolean checkFrame(BlockState state) {
        return CustomAreaHelper.validStateInsidePortal(state, VALID_FRAME) || VALID_FRAME.contains(state.getBlock());
    }
}